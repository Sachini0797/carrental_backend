package com.sachini.booking.controller;

import com.sachini.booking.config.JwtTokenUtil;
import com.sachini.booking.dao.JwtResponse;
import com.sachini.booking.dao.UserDao;
import com.sachini.booking.enums.UserType;
import com.sachini.booking.model.Users;
import com.sachini.booking.repository.UserRepository;
import com.sachini.booking.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/user/user_login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserDao userDto) throws Exception {

        authenticate(userDto.getUsername(), userDto.getPassword());

        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userDto.getUsername());

        String jwttoken = jwtTokenUtil.generateToken(userDetails);


        Users usersDomain = userRepository.findByUsername(userDto.getUsername());

        if(usersDomain !=null) {

            if(usersDomain.getUserType().equals(UserType.CUSTOMER)) {

                JwtResponse jwtResponse=  new JwtResponse(jwttoken, usersDomain.getUserId(),"Success",UserType.CUSTOMER.toString(),userDto.getUsername());

                return ResponseEntity.ok(jwtResponse);
            }

            else {
                JwtResponse jwtResponse=  new JwtResponse(jwttoken, usersDomain.getUserId(),"Success",UserType.ADMIN.toString(),userDto.getUsername());
                return ResponseEntity.ok(jwtResponse);
            }
        }
        return  null;
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
