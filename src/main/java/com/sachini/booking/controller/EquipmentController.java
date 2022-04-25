package com.sachini.booking.controller;

import ch.qos.logback.classic.Logger;
import com.sachini.booking.dao.EquipmentDao;
import com.sachini.booking.dao.VehicleDao;
import com.sachini.booking.model.Equipment;
import com.sachini.booking.service.EquipmentServiceImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
public class EquipmentController {

    private final Logger logger = (Logger) LoggerFactory.getLogger(EquipmentController.class);

    @Autowired
    private EquipmentServiceImpl equipmentService;

    @Value("${basefilepath}")
    private String baseFilePath;

    @RequestMapping(value = "/admin/add_equipment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.POST)
    public String addEquipment(
            @RequestParam("file") MultipartFile file,
            @RequestParam("equipmentName") String equipmentName,
            @RequestParam("description") String description) {

        EquipmentDao equipmentDto = new EquipmentDao();


        if (equipmentName != null) {
            equipmentDto.setEquipmentName(equipmentName);
        }

        if (description != null) {
            equipmentDto.setDescription(description);
        }

        equipmentService.addEquipment(file, equipmentDto);

        logger.info("Successfully Registered New Equipment");


        return "Success";
    }

    @RequestMapping(value = "/admin/list_equipment", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EquipmentDao> listEquipment() {
        return equipmentService.listEquipment();
    }

    @RequestMapping(value = "/reservation/get_available_equipment", method = RequestMethod.POST)
    public List<EquipmentDao> availableEquipment(@RequestParam("reservedate") String reservedate) {

        if (reservedate != null) {
            String reservedDate = reservedate.replace("T", " ");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime vehicleReserveDate = LocalDateTime.parse(reservedDate, formatter);


            List<Equipment> equipmentList = equipmentService.availableEquipment(vehicleReserveDate);
            if (!equipmentList.isEmpty()) {
                return equipmentService.getAvailableEquipmentDto(equipmentList);
            }

        }

        return null;
    }
    
    @RequestMapping(value = "/user/view_all_equipment",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EquipmentDao> viewAllEquipments() {
    	return equipmentService.viewAllEquipment();
    }
    
    @RequestMapping(value = "/admin/delete_equipment", method = RequestMethod.POST)
    public void deleteEquipment(@RequestBody EquipmentDao equipmentDto) {
    	if(equipmentDto.getEquipmentId() != 0) {
    		equipmentService.deleteEquipment(equipmentDto);
    	}
    	else {
    		logger.info("Something went wrong");
    	}
    }
    
    @RequestMapping(value="/admin/update_Equipment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.POST)
    public void updateEquipment(@RequestParam("file") MultipartFile file,
    		@RequestParam("equipmentId") int equipmentId,
    		@RequestParam("equipmentName") String equipmentName,
    		@RequestParam("description") String description) {
    	
    	EquipmentDao equipmentDto = new EquipmentDao();
    	equipmentDto.setEquipmentId(equipmentId);
    	if(equipmentDto.getEquipmentId() !=0) {
    		equipmentDto.setEquipmentName(equipmentName);
    		equipmentDto.setDescription(description);
    		
    		equipmentService.updateEquipment(file, equipmentDto);
    	}
    	else {
    		logger.info("Something went wrong!");
    	}
    }
    		
    		
    		

}
