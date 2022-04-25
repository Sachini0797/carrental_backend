package com.sachini.booking.service;

import java.util.List;

import com.sachini.booking.dao.InquiryDao;

public interface InquiryService {
	
	public void addInquiry(InquiryDao inquiryDao);
	
	List<InquiryDao> viewAllInquiries();

}
