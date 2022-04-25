package com.sachini.booking.model;

import javax.persistence.*;

@Entity
@Table(name = "INQUIRY")
public class Inquiry {
	
	@Id
	@Column(name = "inquiry_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer inquiryId;
	
	@Column(name = "username")
	private String userName;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "telephone_number")
	private String telephoneNumber;
	
	@Column(name = "message")
	private String message;

	public Integer getInquiryId() {
		return inquiryId;
	}

	public void setInquiryId(Integer inquiryId) {
		this.inquiryId = inquiryId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
	
}