package com.sachini.booking.controller;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.sachini.booking.dao.InquiryDao;
import com.sachini.booking.dao.VehicleDao;
import com.sachini.booking.service.InquiryService;
import com.sachini.booking.service.InquiryServiceImpl;

import ch.qos.logback.classic.Logger;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
//@RequestMapping("/api")
public class InquiryController {
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(InquiryController.class);
	
	@Autowired
	private InquiryServiceImpl inquiryService;
	
    @Value("${basefilepath}")
    private String baseFilePath;
    
    @RequestMapping(value = "/add_inquiry", method = RequestMethod.POST)
    public String addinquiry(
    		@RequestParam("userName") String userName,
    		@RequestParam("telephone") String telephone,
    		@RequestParam("email") String email,
    		@RequestParam("message") String message) {
    	
    	InquiryDao inquiryDto = new InquiryDao();
    	
    	if (userName != null) {
    		inquiryDto.setUserName(userName);
    	}
    	if (telephone != null) {
    		inquiryDto.setTelephone(telephone);
    	}
    	if (email != null) {
    		inquiryDto.setEmail(email);
    	}
    	if (message != null) {
    		inquiryDto.setMessage(message);
    	}
    	
    	inquiryService.addInquiry(inquiryDto);
    	
    	logger.info("Successfully Add New Message");
    	
    	return "success";
    	
    }
    
    @RequestMapping(value = "/api/admin/view_all_inquiries",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<InquiryDao> viewAllInquiries(){
        return  inquiryService.viewAllInquiries();
    }
}
