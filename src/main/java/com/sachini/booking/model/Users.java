package com.sachini.booking.model;


import com.sachini.booking.enums.UserStatus;
import com.sachini.booking.enums.UserType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "USER")
public class Users {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(name = "user_name",unique = true)
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String 	password;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "user_status")
    private UserStatus userStatus;

    @Column(name = "user_type")
    private UserType userType;

    @Column(name = "driving_license_photo")
    private String drivingLicensePhoto;

    @Column(name = "statement")
    private String statement;
    
    @Column(name = "nic")
    private String nic;

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservationList;

    @OneToMany(mappedBy = "user")
    private List<Contact> contactList;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getDrivingLicensePhoto() {
        return drivingLicensePhoto;
    }

    public void setDrivingLicensePhoto(String drivingLicensePhoto) {
        this.drivingLicensePhoto = drivingLicensePhoto;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }
    
    

    public String getNic() {
		return nic;
	}

	public void setNic(String nic) {
		this.nic = nic;
	}

	public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }
}
