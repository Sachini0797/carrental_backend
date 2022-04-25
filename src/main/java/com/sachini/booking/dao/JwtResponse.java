package com.sachini.booking.dao;

import java.io.Serializable;

public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwtToken;

    private Integer userId;

    private String requestState;

    private String userType;

    private String username;

    public JwtResponse(String jwtToken, Integer userId, String requestState, String userType, String username) {
        super();
        this.jwtToken = jwtToken;
        this.userId = userId;
        this.requestState = requestState;
        this.userType = userType;
        this.username = username;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRequestState() {
        return requestState;
    }

    public void setRequestState(String requestState) {
        this.requestState = requestState;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
