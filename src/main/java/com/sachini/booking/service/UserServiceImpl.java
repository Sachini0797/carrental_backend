package com.sachini.booking.service;

import com.sachini.booking.dao.ReservationDao;
import com.sachini.booking.dao.UserDao;
import com.sachini.booking.dao.VehicleDao;
import com.sachini.booking.enums.UserStatus;
import com.sachini.booking.enums.UserType;
import com.sachini.booking.model.*;
import com.sachini.booking.repository.ContactRepository;
import com.sachini.booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	private static final String DRIVING_LICENCE_ERROR_MESSAGE = "SUSPENDED";

    public static final String CUSTOMER_APPROVED = "Customer is Approved";

    public static final String CUSTOMER_REJECTED = "Customer is Rejected";
    
    public static final String ACTIVATED = "ACTIVATED";
    
    private static final String CSVPATH = "D:/Project/Eclipse/Backend/src/main/resources/assets/user.csv";
    
    @Value("${basefilepath}")
    private String baseFilePath;
    
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public List<UserDao> listUser() {

        List<Users> usersDomainList = userRepository.findAll();
        List<UserDao> userDtoList = new ArrayList<>();

        if(!usersDomainList.isEmpty()) {


            for(Users usersDomain : usersDomainList) {
                if(!usersDomain.getUsername().contentEquals("Admin")) {

                    UserDao userDto = new UserDao();
                    if(usersDomain.getUserId()!=0) {
                        userDto.setUserId(usersDomain.getUserId());
                    }

                    if(usersDomain.getUsername()!=null) {
                        userDto.setUsername(usersDomain.getUsername());
                    }

                    if(usersDomain.getDateOfBirth().toString()!=null) {
                        LocalDate userBirthYear = usersDomain.getDateOfBirth();
                        LocalDate presentYear = LocalDate.now();
                        Period period = Period.between(userBirthYear, presentYear);
                        userDto.setAge(String.valueOf(period.getYears()));
                    }

                    if(usersDomain.getEmail()!=null) {
                        userDto.setEmail(usersDomain.getEmail());
                    }

                    if(usersDomain.getUserStatus().toString()!=null) {
                        userDto.setUserStatus(usersDomain.getUserStatus().toString());
                    }

                    if(!usersDomain.getContactList().isEmpty()){
                        userDto.setMobile(String.valueOf(usersDomain.getContactList().get(0).getMobile()));
                    }
                    
                    if(usersDomain.getNic().toString()!=null) {
                        userDto.setNic(usersDomain.getNic().toString());
                    }

                    userDtoList.add(userDto);
                }
            }
        }
        return userDtoList;
    }

    @Override
    public Integer registerUser(UserDao userDto) {

        Users usersDomain = new Users();
        Contact contact = new Contact();

        if (userDto != null) {
            usersDomain.setUsername(userDto.getUsername());
            usersDomain.setEmail(userDto.getEmail());
            usersDomain.setPassword(passwordEncoder.encode(userDto.getPassword()));
            usersDomain.setMobile(userDto.getMobile());
            usersDomain.setNic(userDto.getNic());

            LocalDate dateOfBirth = LocalDate.parse(userDto.getDateOfBirth());
            usersDomain.setDateOfBirth(dateOfBirth);

            usersDomain.setUserStatus(UserStatus.NEWCUSTOMER);
            usersDomain.setUserType(UserType.CUSTOMER);
            usersDomain.setDrivingLicensePhoto(userDto.getDrivingLicensePhoto());
            userRepository.save(usersDomain);

            contact.setMobile(Integer.parseInt(userDto.getMobile()));
            contact.setUser(usersDomain);
            contactRepository.save(contact);
            sendEmailToUser(userDto.getEmail());

        }

        Users users = userRepository.findByUsername(userDto.getUsername());
        return users.getUserId();
    }

    @Override
    public boolean userAuthentication(UserDao userDto) {

        if (userDto.getUsername() != null) {
            Users users = userRepository.findByUsername(userDto.getUsername());
            if (users != null && !users.getPassword().isEmpty()) {
                return passwordEncoder.matches(userDto.getPassword(), users.getPassword());
            }
        }
        return false;
    }

    @Override
    public void updateUser(UserDao userDto) {

    }

    @Override
    public UserType userTypeVerification(UserDao userDto) {

        if (userDto.getUsername()!= null) {
            Users users = userRepository.findByUsername(userDto.getUsername());
            if(users.getUserType() != null) {
                return users.getUserType();
            }
        }
        return null;
    }

    @Override
    public String confirmDetailsApproveOrReject(UserDao userDto) {

        if(userDto.getUserId()!=0) {

            Optional<Users> userDomainOpt = userRepository.findById(userDto.getUserId());

            if(userDomainOpt.isPresent()) {
                Users userDomain = userDomainOpt.get();

                if(userDto.getUserStatus().contentEquals("APPROVED")) {
                    userDomain.setUserStatus(UserStatus.APPROVEDCUSTOMER);
                    userRepository.save(userDomain);
                    return CUSTOMER_APPROVED;
                }
                else if (userDto.getUserStatus().contentEquals("REJECTED")) {
                    userDomain.setUserStatus(UserStatus.REJECTEDCUSTOMER);
                    userRepository.save(userDomain);
                    return CUSTOMER_REJECTED;
                }
            }
        }

        return null;
    }

    @Override
    public List<UserDao> viewPendingUser() {

        List<Users> userList = userRepository.getPendingUsers();
        List<UserDao> userDtoList = new ArrayList<>();

        if(!userList.isEmpty()) {


            for(Users userDomain: userList) {
                if(!userDomain.getUsername().contentEquals("Admin")) {

                    UserDao user = new UserDao();
                    if(userDomain.getUserId()!=0) {
                        user.setUserId(userDomain.getUserId());
                    }

                    if(userDomain.getUsername()!=null) {
                        user.setUsername(userDomain.getUsername());
                    }

                    if(userDomain.getDateOfBirth().toString()!=null) {
                        LocalDate userBirthYear = userDomain.getDateOfBirth();
                        LocalDate presentYear = LocalDate.now();
                        Period period = Period.between(userBirthYear, presentYear);
                        user.setAge(String.valueOf(period.getYears()));
                    }

                    if(userDomain.getEmail()!=null) {
                        user.setEmail(userDomain.getEmail());
                    }

                    if(userDomain.getStatement()!=null) {
                        user.setStatement(userDomain.getStatement());
                    }

                    if(userDomain.getUserStatus().toString()!=null) {
                        user.setUserStatus(userDomain.getUserStatus().toString());
                    }

                    if(!userDomain.getContactList().isEmpty()){
                        user.setMobile(String.valueOf(userDomain.getContactList().get(0).getMobile()));
                    }
                    
                    if(userDomain.getNic()!=null) {
                        user.setNic(userDomain.getNic());
                    }

                    userDtoList.add(user);

                }

            }
        }

        return userDtoList;
    }

    @Override
    public List<UserDao> viewUser() {

        List<Users> userList = userRepository.findAll();
        List<UserDao> userDtoList = new ArrayList<>();

        if(!userList.isEmpty()) {


            for(Users userDomain: userList) {
                if(!userDomain.getUsername().contentEquals("Admin")) {

                    UserDao userdto = new UserDao();
                    if(userDomain.getUserId()!=0) {
                        userdto.setUserId(userDomain.getUserId());
                    }

                    if(userDomain.getUsername()!=null) {
                        userdto.setUsername(userDomain.getUsername());
                    }

                    if(userDomain.getDateOfBirth().toString()!=null) {
                        LocalDate userBirthYear = userDomain.getDateOfBirth();
                        LocalDate presentYear = LocalDate.now();
                        Period period = Period.between(userBirthYear, presentYear);
                        userdto.setAge(String.valueOf(period.getYears()));
                    }

                    if(userDomain.getEmail()!=null) {
                        userdto.setEmail(userDomain.getEmail());
                    }

                    if(userDomain.getUserStatus().toString()!=null) {
                        userdto.setUserStatus(userDomain.getUserStatus().toString());
                    }

                    if(!userDomain.getContactList().isEmpty()){
                        userdto.setMobile(String.valueOf(userDomain.getContactList().get(0).getMobile()));
                    }
                    
                    if(userDomain.getNic()!=null) {
                        userdto.setNic(userDomain.getNic());
                    }

                    userDtoList.add(userdto);
                }
            }
        }

        return userDtoList;
    }

    public String checkUserStatus(UserDao user) throws IOException {

        Optional<Users> userDomainOpt = userRepository.findById(user.getUserId());

        if(userDomainOpt.isPresent()) {

            Users usersDomainObject = userDomainOpt.get();

            boolean flag = false;
            String license = null;

            if (user != null) {
                license = usersDomainObject.getNic();
                try {
                    flag = checkDriver(license);
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }

            if (flag) {
                //sendEmail(license);
                return DRIVING_LICENCE_ERROR_MESSAGE;
            }

                    Optional<Users> userOpt = userRepository.findById(user.getUserId());
                    if(userOpt.isPresent()) {

                        Users usersDomain = userOpt.get();

                        if (usersDomain.getUserStatus().equals(UserStatus.NEWCUSTOMER)) {
                            return UserStatus.NEWCUSTOMER.toString();
                        } else if (usersDomain.getUserStatus().equals(UserStatus.LOYALCUSTOMER)) {
                            return UserStatus.LOYALCUSTOMER.toString();
                        } else if (usersDomain.getUserStatus().equals(UserStatus.PENDINGCUSTOMER)) {
                            return UserStatus.PENDINGCUSTOMER.toString();
                        } else if (usersDomain.getUserStatus().equals(UserStatus.APPROVEDCUSTOMER)) {
                            return UserStatus.APPROVEDCUSTOMER.toString();
                        } else if (usersDomain.getUserStatus().equals(UserStatus.REJECTEDCUSTOMER)) {
                            return UserStatus.REJECTEDCUSTOMER.toString();
                        }
                    }
            return "Success";
        }
        return null;
    }


    public void uploadUserUtility(MultipartFile file, Integer userId) throws IOException {

        Optional<Users> userOpt = userRepository.findById(userId);

        Users usersDomain = null;
        if (userOpt.isPresent()) {
            usersDomain = userOpt.get();

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

//            Path path = Paths
//                    .get("D:/Frontend/car-booking/src/Images/utilityconfimation/" + fileName);
//            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            
            upload(file);

            usersDomain.setUserStatus(UserStatus.PENDINGCUSTOMER);
            usersDomain.setStatement(fileName);
            userRepository.save(usersDomain);

        }

    }
    
    public void upload(MultipartFile file) {
    	try {
    		Path copyLocation = Paths
    				.get(baseFilePath + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
    		Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
    		
    	}catch(Exception e) {
    		
    	}
    }
    
    @Override
	public void deleteUser(UserDao userDto) {
		// TODO Auto-generated method stub
		if(userDto.getUserId() != 0) {
			userRepository.deleteById(userDto.getUserId());
		}
	}
    
    @Override
    public List<UserDao> listMyDetails(UserDao userDto) {
    	
    	List<UserDao> myDetailsList = new ArrayList<>();
    	Integer userId = userDto.getUserId();
    	 String status = ACTIVATED;
    	List<Users> userList = userRepository.findByUserId(userId, status);
    	
    	if(!userList.isEmpty()) {
    		for (Users user : userList) {
    			
    			userDto.setUserId(user.getUserId());
    			
    			
    			if(user.getUsername() != null) {
    				userDto.setUsername(user.getUsername());
    			}
    			
    			if(user.getEmail() != null) {
    				userDto.setEmail(user.getEmail());
    			}
    			
    			if(user.getMobile() !=null) {
    				userDto.setMobile(user.getMobile());
    			}
    			
    			
    			
    			myDetailsList.add(userDto);
    		}
    		
    	}
    	
    	
		return myDetailsList;
    	
    	
    }
    
    private void sendEmailToUser(String sendEemail) {

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(sendEemail);
        email.setSubject("Car Rental 365");
        email.setText("Your registration has been successfully completed."
                + " Thank you for your valuable joining");

        javaMailSender.send(email);
    }
    
    private boolean checkDriver(String driving) throws IOException{

        String line = "";
        @SuppressWarnings("resource")
        BufferedReader bufferReader = new BufferedReader(new FileReader(
                CSVPATH));
        while ((line = bufferReader.readLine()) != null)
        {
            if (driving.equals(line)) {
                return true;
            }
        }
        bufferReader.close();
        return false;
    }

    public List<UserDao> viewMyProfile(UserDao userDto) {

        List<Users> userList = userRepository.profileByUserId(userDto.getUserId());
        List<UserDao> userDtoList = new ArrayList<>();
        for(Users profile : userList) {

            if(profile.getUserId()!=0) {
                userDto.setUserId(profile.getUserId());
            }

            if(profile.getUsername()!=null) {
                userDto.setUsername(profile.getUsername());
            }

            if(profile.getDateOfBirth().toString()!=null) {
                LocalDate userBirthYear = profile.getDateOfBirth();
                LocalDate presentYear = LocalDate.now();
                Period period = Period.between(userBirthYear, presentYear);
                userDto.setAge(String.valueOf(period.getYears()));
            }

            if(profile.getEmail()!=null) {
                userDto.setEmail(profile.getEmail());
            }

            if(profile.getDateOfBirth().toString()!=null) {
                userDto.setDateOfBirth(profile.getDateOfBirth().toString());
            }

            if(profile.getUserStatus().toString()!=null) {
                userDto.setUserStatus(profile.getUserStatus().toString());
            }

            if(!profile.getContactList().isEmpty()){
                userDto.setMobile(String.valueOf(profile.getContactList().get(0).getMobile()));
            }

            if(profile.getNic().toString()!=null) {
                userDto.setNic(profile.getNic().toString());
            }

            if(profile.getDrivingLicensePhoto() != null) {
                userDto.setDrivingLicensePhoto(profile.getDrivingLicensePhoto());
            }
            userDtoList.add(userDto);
        }
        return userDtoList;
    }
}
