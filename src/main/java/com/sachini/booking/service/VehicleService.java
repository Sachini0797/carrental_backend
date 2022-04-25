package com.sachini.booking.service;


import com.sachini.booking.dao.VehicleDao;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface VehicleService {

    public void addVehicle(MultipartFile files, VehicleDao vehicleDto);

    public List<VehicleDao> listVehicle();

    public List<VehicleDao> listAllVehicle();

 

    List<VehicleDao> viewAllVehicle();

    
//    1st code
    public void deleteVehicle(VehicleDao vehicleDto);
    
    public void updateVehicle(MultipartFile files,VehicleDao vehicleDto);

}
