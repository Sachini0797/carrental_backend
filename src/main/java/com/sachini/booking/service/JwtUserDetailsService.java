package com.sachini.booking.service;

import com.sachini.booking.model.Users;
import com.sachini.booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (username != null) {
            Users user = userRepository.findByUsername(username);

            if(user!=null) {
                return new User(user.getUsername(), user.getPassword(),
                        new ArrayList<>());
            }
            return null;

        } else {
            throw new UsernameNotFoundException("Error: username doesn't exist in the system: " + username);
        }

    }

}
