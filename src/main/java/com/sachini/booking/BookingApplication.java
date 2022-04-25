package com.sachini.booking;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.CacheControl;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BookingApplication implements WebMvcConfigurer{
	
	 @Autowired
	    private JavaMailSender javaMailSender;

    public static void main(String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }
    
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	registry.addResourceHandler("/images/**").addResourceLocations("/WEB-INF/images/")
    	.setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());
    	
    }
    
//    @EventListener(ApplicationReadyEvent.class)
//    private void sendEmailToUser() {
//
//        SimpleMailMessage email = new SimpleMailMessage();
//        email.setTo("sachinipaul97@gmail.com");
//        email.setSubject("Car Rental 365");
//        email.setText("bla bla"
//                + " bla bla");
//
//        javaMailSender.send(email);
//    }

}
