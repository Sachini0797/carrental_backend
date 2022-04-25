package com.sachini.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sachini.booking.model.Inquiry;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Integer>{

}
