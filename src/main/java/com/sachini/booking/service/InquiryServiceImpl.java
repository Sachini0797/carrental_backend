package com.sachini.booking.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sachini.booking.dao.EquipmentDao;
import com.sachini.booking.dao.InquiryDao;
import com.sachini.booking.model.Equipment;
import com.sachini.booking.model.Inquiry;
import com.sachini.booking.repository.InquiryRepository;

@Service
public class InquiryServiceImpl implements InquiryService {
	
	@Autowired
	private InquiryRepository inquiryRepository;
	
	@Override
	public void addInquiry(InquiryDao inquiryDao) {
		Inquiry inquiryModel = new Inquiry();
		
		if (inquiryDao.getUserName() != null) {
			inquiryModel.setUserName(inquiryDao.getUserName());
		}
		if (inquiryDao.getEmail() != null) {
			inquiryModel.setEmail(inquiryDao.getEmail());
		}
		if (inquiryDao.getTelephone() != null) {
			inquiryModel.setTelephoneNumber(inquiryDao.getTelephone());
		}
		if (inquiryDao.getMessage() != null) {
			inquiryModel.setMessage(inquiryDao.getMessage());
		}
		
		inquiryRepository.save(inquiryModel);
	}
	
	@Override
	public List<InquiryDao> viewAllInquiries() {
		
		List<InquiryDao> allInquiryDtoList = new ArrayList<>();
        List<Inquiry> allInquiryList = inquiryRepository.findAll();
        
        if (!allInquiryList.isEmpty()) {
        	
        	for(Inquiry inquiry: allInquiryList) {
        		if (!inquiry.getUserName().contentEquals("Admin")) {
        			
        			InquiryDao inquiryDto = new InquiryDao();
        			
        			if (inquiry.getInquiryId() !=0) {
        				inquiryDto.setInquiryId(inquiry.getInquiryId());
        			}
        			
        			if (inquiry.getUserName() != null) {
        				inquiryDto.setUserName(inquiry.getUserName());
        			}
        			
        			if (inquiry.getTelephoneNumber() != null) {
        				inquiryDto.setTelephone(inquiry.getTelephoneNumber());
        			}
        			
        			if (inquiry.getEmail() != null) {
        				inquiryDto.setEmail(inquiry.getEmail());
        			}
        			
        			if (inquiry.getMessage() != null) {
        				inquiryDto.setMessage(inquiry.getMessage());
        			}
        			
        			allInquiryDtoList.add(inquiryDto);
        		}
        	}
        }
        return allInquiryDtoList;
	}
	
	

}
