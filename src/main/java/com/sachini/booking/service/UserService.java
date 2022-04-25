package com.sachini.booking.service;


import com.sachini.booking.dao.UserDao;
import com.sachini.booking.enums.UserType;

import java.util.List;

public interface UserService {

    public List<UserDao> listUser();

    public Integer registerUser(UserDao userDto);

    public boolean userAuthentication(UserDao userDto);

    public void updateUser(UserDao userDto);

    public UserType userTypeVerification(UserDao userDto);

    String confirmDetailsApproveOrReject(UserDao userDto);

    List<UserDao> viewPendingUser();

    List<UserDao> viewUser();
    
    public void deleteUser(UserDao userDto);
    
    public List<UserDao> listMyDetails(UserDao userDto);
}
