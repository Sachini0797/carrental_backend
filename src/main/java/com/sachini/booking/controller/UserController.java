package com.sachini.booking.controller;

import ch.qos.logback.classic.Logger;
import com.sachini.booking.config.JwtTokenUtil;
import com.sachini.booking.dao.JwtResponse;
import com.sachini.booking.dao.ReservationDao;
import com.sachini.booking.dao.UserDao;
import com.sachini.booking.dao.VehicleDao;
import com.sachini.booking.enums.UserType;
import com.sachini.booking.service.JwtUserDetailsService;
import com.sachini.booking.service.UserServiceImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class UserController {

	private final Logger logger = (Logger) LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Value("${basefilepath}")
	private String baseFilePath;

	@RequestMapping(value = "/user/user_registration", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.POST)
	public ResponseEntity<?> registerUser(
			@RequestParam("file") MultipartFile file,
			@RequestParam("username") String username,
            @RequestParam("email") String email,
			@RequestParam("password") String password,
            @RequestParam("dateOfBirth") String dateOfBirth,
            @RequestParam("mobile") String mobile,
            @RequestParam("nic") String nic) {

		UserDao userDto = new UserDao();

		if(username!=null) {
			userDto.setUsername(username);
		}
		if(email!=null) {
			userDto.setEmail(email);
		}
		if(password!=null) {
			userDto.setPassword(password);
		}
		if(dateOfBirth!=null) {
			userDto.setDateOfBirth(dateOfBirth);
		}
		if(mobile!=null) {
			userDto.setMobile(mobile);
		}
		if(nic!=null) {
			userDto.setNic(nic);
		}
		

		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		Path path = Paths.get(baseFilePath + fileName);
		if(!file.isEmpty()) {
			userDto.setDrivingLicensePhoto(fileName);
		}

		Integer userId = userService.registerUser(userDto);

		try {
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			logger.info("Exception ", e);
		}
		logger.info("New User Registration", userDto.getUsername());

		final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userDto.getUsername());
	    String jwttoken = jwtTokenUtil.generateToken(userDetails);

		JwtResponse jwtResponse=  new JwtResponse(jwttoken,userId,"Success", UserType.CUSTOMER.toString(),userDto.getUsername());

		return ResponseEntity.ok(jwtResponse);

	}

	@RequestMapping(value = "/admin/list_user",method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserDao> listUser(){
		return userService.viewUser();
	}

	@RequestMapping(value = "/user/check_user_status",method = RequestMethod.POST)
	public String checkUserStatus( @RequestBody UserDao userDto) throws IOException{

		if(userDto!=null) {

			return userService.checkUserStatus(userDto);
		}
		return "Error: User status not found";
	}

	@RequestMapping(value ="/user/confirmation_details",method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String submitConfirmationDetails(@RequestParam("file") MultipartFile file, @RequestParam("userid") Integer userid) throws IOException {

		userService.uploadUserUtility(file,userid);

		return "Success";
	}

	@RequestMapping(value = "/admin/approve_reject_confirmationDetails",method = RequestMethod.POST)
	public String confirmationDetailsApprovalReject(@RequestBody UserDao userDto) {

		return userService.confirmDetailsApproveOrReject(userDto);

	}

	@RequestMapping(value = "/admin/view_pending_users",method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserDao> viewPendingUser(){
		return userService.viewPendingUser();
	}
	
	
	@RequestMapping(value = "/admin/delete_user", method = RequestMethod.POST)
    public void deleteUser(@RequestBody UserDao userDto) {
    	if(userDto.getUserId() != 0) {
    		userService.deleteUser(userDto);
    	}
    	else {
    		logger.info("Something went wrong");
    	}
    }
	


	@RequestMapping(value = "/user/my_profile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserDao> viewMyProfile(@RequestBody UserDao userDto) {
		if (userDto.getUserId() != 0) {
			return userService.viewMyProfile(userDto);
		}
		return null;
	}
	
}
