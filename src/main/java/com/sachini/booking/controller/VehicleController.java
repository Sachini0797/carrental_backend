package com.sachini.booking.controller;

import ch.qos.logback.classic.Logger;
import com.sachini.booking.dao.EquipmentDao;
import com.sachini.booking.dao.VehicleDao;
import com.sachini.booking.model.Equipment;
import com.sachini.booking.model.Vehicle;
import com.sachini.booking.service.VehicleServiceImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Console;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class VehicleController {

    private final Logger logger = (Logger) LoggerFactory.getLogger(VehicleController.class);

    @Autowired
    private VehicleServiceImpl vehicleService;

    @Value("${basefilepath}")
    private String baseFilePath;

    @RequestMapping(value = "/admin/add_vehicle", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.POST)
    public String addVehicle(
            @RequestParam("file") MultipartFile file,
            @RequestParam("vehicleName") String vehicleName,
            @RequestParam("fuelType") String fuelType,
            @RequestParam("vehicleDoors") String vehicleDoors,
            @RequestParam("vehicleSeats") String vehicleSeats,
            @RequestParam("vehicleType") String vehicleType,
            @RequestParam("transmission") String transmission,
            @RequestParam("ratePerMonth") String ratePerMonth,
            @RequestParam("ratePerWeek") String ratePerWeek,
            @RequestParam("pricePerDay") String pricePerDay) {

        VehicleDao vehicleDto = new VehicleDao();
        if (fuelType != null) {
            vehicleDto.setFuelType(fuelType);
        }
        if (vehicleType != null) {
            vehicleDto.setVehicleType(vehicleType);
        }
        if (vehicleName != null) {
            vehicleDto.setVehicleName(vehicleName);
        }
        if (transmission != null) {
            vehicleDto.setTransmission(transmission);
        }
        if (ratePerMonth != null) {
            double monthlyrate = Double.parseDouble(ratePerMonth);
            if (monthlyrate != 0.0) {
                vehicleDto.setRatePerMonth(monthlyrate);
            }
        }
        if (ratePerWeek != null) {
            double weeklyrate = Double.parseDouble(ratePerWeek);
            if (weeklyrate != 0.0) {
            	vehicleDto.setRatePerWeek(weeklyrate);
            }
        }
        if (pricePerDay != null) {
            double price = Double.parseDouble(pricePerDay);
            if (price != 0.0) {
                vehicleDto.setPricePerDay(price);
            }
        }
        if (vehicleDoors != null) {
            int doors = Integer.parseInt(vehicleDoors);
            if (doors != 0) {
                vehicleDto.setVehicleDoors(doors);
            }
        }
        if (vehicleSeats != null) {
            int seats = Integer.parseInt(vehicleSeats);
            if (seats != 0) {
                vehicleDto.setVehicleSeats(seats);
            }
        }

        vehicleService.addVehicle(file, vehicleDto);

        logger.info("Successfully Registered New Vehicle");

        return "Success";
    }

    @RequestMapping(value = "/admin/list_vehicle", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VehicleDao> listVehicle() {
        return vehicleService.listVehicle();
    }

//    @RequestMapping(value = "/user/view_small_town_car",method = RequestMethod.GET)
//    public List<VehicleDao> viewSmallTownCars(){
//        return  vehicleService.viewSmallTownCar();
//    }
//
//    @RequestMapping(value = "/user/small_family_hatchback",method = RequestMethod.GET)
//    public List<VehicleDao> viewSmallFamilyHatchback(){
//        return  vehicleService.viewSmallFamilyHatchback();
//    }
//
//    @RequestMapping(value = "/user/large_family_saloon",method = RequestMethod.GET)
//    public List<VehicleDao> viewLargeFamilySaloon(){
//        return  vehicleService.viewLargeFamilySaloon();
//    }
//
//    @RequestMapping(value = "/user/large_family_estate",method = RequestMethod.GET)
//    public List<VehicleDao> viewLargeFamilyEstate(){
//        return  vehicleService.viewLargeFamilyEstate();
//    }
//
//    @RequestMapping(value = "/user/medium_van",method = RequestMethod.GET)
//    public List<VehicleDao> viewMediumVans(){
//        return  vehicleService.viewMediumVan();
//    }
//
//    @RequestMapping(value = "/user/other_type_vehicle",method = RequestMethod.GET)
//    public List<VehicleDao> viewOtherTypeVehicles(){
//        return  vehicleService.viewOtherTypeVehicle();
//    }

    @RequestMapping(value = "/user/view_all_vehicle",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VehicleDao> viewAllVehicles(){
        return  vehicleService.viewAllVehicle();
    }
    
//    3rd code
    @RequestMapping(value = "/admin/delete_vehicle", method = RequestMethod.POST)
    public void deleteVehicle(@RequestBody VehicleDao vehicleDto) {
    	if(vehicleDto.getVehicleId() != 0) {
    		vehicleService.deleteVehicle(vehicleDto);
    	}
    	else {
    		logger.info("Something went wrong");
    	}
    }
     
    @RequestMapping(value = "/admin/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.POST)
    public void updateVehicle(@RequestParam("file") MultipartFile file,
    		@RequestParam("vehicleId") int vehicleId,
    		@RequestParam("vehicleName") String vehicleName,
    		@RequestParam("ratePerMonth") String ratePerMonth,
            @RequestParam("ratePerWeek") String ratePerWeek,
    		@RequestParam("pricePerDay") String pricePerDay,
    		@RequestParam("transmission") String transmission,
    		@RequestParam("vehicleType") String vehicleType,
    		@RequestParam("vehicleDoors") String vehicleDoors,
    		@RequestParam("vehicleSeats") String vehicleSeats,
    		@RequestParam("fuelType") String fuelType) {
    	
    	VehicleDao vehicleDto = new VehicleDao();
    	vehicleDto.setVehicleId(vehicleId);
    	if(vehicleDto.getVehicleId() != 0) {
    		if (fuelType != null) {
                vehicleDto.setFuelType(fuelType);
            }
            if (vehicleType != null) {
                vehicleDto.setVehicleType(vehicleType);
            }
            if (vehicleName != null) {
                vehicleDto.setVehicleName(vehicleName);
            }
            if (transmission != null) {
                vehicleDto.setTransmission(transmission);
            }
            if (ratePerMonth != null) {
                double monthlyrate = Double.parseDouble(ratePerMonth);
                if (monthlyrate != 0.0) {
                    vehicleDto.setRatePerMonth(monthlyrate);
                }
            }
            if (ratePerWeek != null) {
                double weeklyrate = Double.parseDouble(ratePerWeek);
                if (weeklyrate != 0.0) {
                	vehicleDto.setRatePerWeek(weeklyrate);
                }
            }
            if (pricePerDay != null) {
                double price = Double.parseDouble(pricePerDay);
                if (price != 0.0) {
                    vehicleDto.setPricePerDay(price);
                }
            }
            if (vehicleDoors != null) {
                int doors = Integer.parseInt(vehicleDoors);
                if (doors != 0) {
                    vehicleDto.setVehicleDoors(doors);
                }
            }
            if (vehicleSeats != null) {
                int seats = Integer.parseInt(vehicleSeats);
                if (seats != 0) {
                    vehicleDto.setVehicleSeats(seats);
                }
            }

//    		vehicleDto.setVehicleName(vehicleName);
//    		vehicleDto.setFuelType(fuelType);
//    		vehicleDto.setTransmission(transmission);
//    		vehicleDto.setVehicleDoors(vehicleDoors);
//    		vehicleDto.setVehicleSeats(vehicleSeats);
//    		
//    		double price = Double.parseDouble(pricePerDay);
//    		if(price != 0.0) {
//    			vehicleDto.setPricePerDay(price);
//    		}
    		
    		vehicleService.updateVehicle(file, vehicleDto);

    	}
    	else {
    		logger.info("Something went wrong!");
    	}
    	
    }

    @RequestMapping(value = "/reservation/get_available_vehicle", method = RequestMethod.POST)
    public List<VehicleDao> availableVehicle(@RequestParam("reservedate") String reservedate) {

        if (reservedate != null) {
            String reservedDate = reservedate.replace("T", " ");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime vehicleReserveDate = LocalDateTime.parse(reservedDate, formatter);


            List<Vehicle> vehicleList = vehicleService.availableVehicle(vehicleReserveDate);
            if (!vehicleList.isEmpty()) {
                return vehicleService.getAvailableVehicleDto(vehicleList);
            }

        }

        return null;
    }

}

















