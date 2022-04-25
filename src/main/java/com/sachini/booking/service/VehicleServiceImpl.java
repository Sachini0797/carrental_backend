package com.sachini.booking.service;

import com.sachini.booking.dao.EquipmentDao;
import com.sachini.booking.dao.VehicleDao;
import com.sachini.booking.enums.FuelType;
import com.sachini.booking.enums.VehicleStatus;
import com.sachini.booking.enums.VehicleTransmission;
import com.sachini.booking.enums.VehicleTypeEnum;
import com.sachini.booking.model.Equipment;
import com.sachini.booking.model.Reservation;
import com.sachini.booking.model.Vehicle;
import com.sachini.booking.model.VehicleType;
import com.sachini.booking.repository.ReservationRepository;
import com.sachini.booking.repository.VehicleRepository;
import com.sachini.booking.repository.VehicleTypeRepository;

import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;


@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    public static final String ACTIVATED = "ACTIVATED";

    public static final String COMPLETED = "COMPLETED";

    public static final String PENALTY = "FINE";

    public static final String CUSTOMER_APPROVED = "Customer is Approved";

    public static final String CUSTOMER_REJECTED = "Customer is Rejected";
    
    @Value("${basefilepath}")
    private String baseFilePath;

    @Override
    public void addVehicle(MultipartFile files, VehicleDao vehicleDao) {

        Vehicle vehicleModel = new Vehicle();
        if (vehicleDao != null) {

            VehicleType vehicleType = new VehicleType();

            if (vehicleDao.getFuelType() != null) {
                vehicleModel.setFuelType(FuelType.fromText(vehicleDao.getFuelType()));
            }

            if (vehicleDao.getTransmission() != null) {
                vehicleModel.setTransmission(VehicleTransmission.fromText(vehicleDao.getTransmission()));
            }
            
            if (vehicleDao.getVehicleStatus() != null) {
                vehicleModel.setVehicleStatus(VehicleStatus.AVAILABLE);
            }

            if (vehicleDao.getVehicleType() != null) {
                vehicleType.setVehicleType(VehicleTypeEnum.fromText(vehicleDao.getVehicleType()));
                vehicleModel.setVehicleType(vehicleType);
                vehicleModel.setVehicleStatus(VehicleStatus.AVAILABLE);
            }

            if (vehicleDao.getVehicleName() != null) {
                vehicleModel.setVehicleName(vehicleDao.getVehicleName());
            }

            if (vehicleDao.getPricePerDay() != 0.0) {
                vehicleModel.setPricePerDay(vehicleDao.getPricePerDay());
            }
            
            if (vehicleDao.getRatePerMonth() !=0.0) {
            	vehicleModel.setRatePerMonth(vehicleDao.getRatePerMonth());
            }
            
            if (vehicleDao.getRatePerWeek() !=0.0) {
            	vehicleModel.setRatePerWeek(vehicleDao.getRatePerWeek());
            }

            if (vehicleDao.getVehicleDoors() != 0) {
                vehicleModel.setVehicleDoors(vehicleDao.getVehicleDoors());
            }

            if (vehicleDao.getVehicleSeats() != 0) {
                vehicleModel.setVehicleSeats(vehicleDao.getVehicleSeats());
            }

            upload(files);
            vehicleModel.setVehicleImage(files.getOriginalFilename());
            
            vehicleTypeRepository.save(vehicleType);
            vehicleRepository.save(vehicleModel);
        }
    }

    @Override
    public List<VehicleDao> listVehicle() {

        List<Vehicle> vehicleModelList = vehicleRepository.findAll();
        List<VehicleDao> vehicleDtoList = new ArrayList<>();
        if (!vehicleModelList.isEmpty()) {

            for (Vehicle vehicle : vehicleModelList) {
                VehicleDao vehicleDao = new VehicleDao();

                vehicleDao.setVehicleId(vehicle.getVehicleId());
                vehicleDao.setVehicleName(vehicle.getVehicleName());
                vehicleDao.setFuelType(vehicle.getFuelType().toString());
                vehicleDao.setPricePerDay(vehicle.getPricePerDay());
                vehicleDao.setVehicleDoors(vehicle.getVehicleDoors());
                vehicleDao.setVehicleSeats(vehicle.getVehicleSeats());
                vehicleDao.setTransmission(vehicle.getTransmission().toString());
                vehicleDao.setVehicleStatus(vehicle.getVehicleStatus().toString());
                vehicleDao.setVehicleImage(vehicle.getVehicleImage());
                vehicleDao.setRatePerMonth(vehicle.getRatePerMonth());
                vehicleDao.setRatePerWeek(vehicle.getRatePerWeek());

//                Optional<VehicleType> vehicleTypeOpt = vehicleTypeRepository.findById(vehicle.getVehicleId());
//                if (vehicleTypeOpt.isPresent()) {
//                    VehicleType vehicleType = vehicleTypeOpt.get();
//                    vehicleDao.setVehicleType(vehicleType.getVehicleType().toString().replace("_", " "));
//
//                }
                
                vehicleDao.setVehicleType(vehicle.getVehicleType().getVehicleType().toString());

                vehicleDtoList.add(vehicleDao);

            }
        }
        return vehicleDtoList;
    }

    @Override
    public List<VehicleDao> listAllVehicle() {

        List<VehicleDao> vehicleDtoList = new ArrayList<>();
        List<Vehicle> vehicleModelList = vehicleRepository.findAll();

        for (Vehicle vehicle : vehicleModelList) {

            VehicleDao vehicleDao = new VehicleDao();
            vehicleDao.setVehicleId(vehicle.getVehicleId());
            vehicleDao.setVehicleType(vehicle.getVehicleType().getVehicleType().toString().replace("_", " "));

            if (vehicle.getVehicleName() != null) {
                vehicleDao.setVehicleName(vehicle.getVehicleName());
            }
            if (vehicle.getFuelType() != null) {
                vehicleDao.setFuelType(vehicle.getFuelType().toString());
            }
            if (vehicle.getVehicleDoors() != 0) {
                vehicleDao.setVehicleDoors(vehicle.getVehicleDoors());
            }
            if (vehicle.getVehicleSeats() != 0) {
                vehicleDao.setVehicleSeats(vehicle.getVehicleSeats());
            }
            if (vehicle.getTransmission() != null) {
                vehicleDao.setTransmission(vehicle.getTransmission().toString());
            }
            if (vehicle.getPricePerDay() != 0.0) {
                vehicleDao.setPricePerDay(vehicle.getPricePerDay());
            }
            if (vehicle.getRatePerMonth() !=0.0) {
            	vehicleDao.setRatePerMonth(vehicle.getRatePerMonth());
            }
            if (vehicle.getRatePerWeek() !=0.0) {
            	vehicleDao.setRatePerWeek(vehicle.getRatePerWeek());
            }
            if (vehicle.getVehicleImage() != null) {
                vehicleDao.setVehicleImage(vehicle.getVehicleImage());
            }
            vehicleDtoList.add(vehicleDao);
        }
        return vehicleDtoList;
    }

    @Override
    public List<VehicleDao> viewAllVehicle() {

        List<VehicleDao> allVehiclesDtoList = new ArrayList<>();
        List<Vehicle> allVehicleList = vehicleRepository.findAll();

        for(Vehicle vehicle : allVehicleList) {

            VehicleDao vehicleDao = new VehicleDao();
            vehicleDao.setVehicleId(vehicle.getVehicleId());
            vehicleDao.setVehicleType(vehicle.getVehicleType().getVehicleType().toString().replace("_", " "));

            if(vehicle.getFuelType()!=null) {
                vehicleDao.setFuelType(vehicle.getFuelType().toString());
            }
            if(vehicle.getVehicleName() != null) {
                vehicleDao.setVehicleName(vehicle.getVehicleName());
            }
            if(vehicle.getVehicleDoors() != 0) {
                vehicleDao.setVehicleDoors(vehicle.getVehicleDoors());
            }
            if(vehicle.getVehicleSeats() != 0) {
                vehicleDao.setVehicleSeats(vehicle.getVehicleSeats());
            }
            if(vehicle.getPricePerDay() != 0.0) {
                vehicleDao.setPricePerDay(vehicle.getPricePerDay());
            }
            if(vehicle.getRatePerMonth() != 0.0) {
                vehicleDao.setRatePerMonth(vehicle.getRatePerMonth());
            }
            if(vehicle.getRatePerWeek() != 0.0) {
                vehicleDao.setRatePerWeek(vehicle.getRatePerWeek());
            }
            if(vehicle.getTransmission() != null) {
                vehicleDao.setTransmission(vehicle.getTransmission().toString());
            }
            if(vehicle.getVehicleStatus() != null) {
                vehicleDao.setVehicleStatus(vehicle.getVehicleStatus().toString());
            }
            if(vehicle.getVehicleImage() != null) {
                vehicleDao.setVehicleImage(vehicle.getVehicleImage());
            }
            allVehiclesDtoList.add(vehicleDao);
        }
        return allVehiclesDtoList;
    }

 

//    2nd code
	@Override
	public void deleteVehicle(VehicleDao vehicleDto) {
		// TODO Auto-generated method stub
		if(vehicleDto.getVehicleId() != 0) {
			vehicleRepository.deleteById(vehicleDto.getVehicleId());
		}
	}
	
	@Override
	public void updateVehicle(MultipartFile files,VehicleDao vehicleDto) {
		
		if(vehicleDto.getVehicleId() != 0){
			Vehicle existingVehicle = vehicleRepository.findById(vehicleDto.getVehicleId()).orElse(null);
			VehicleType vehicleType = new VehicleType();
			
			existingVehicle.setPricePerDay(vehicleDto.getPricePerDay());
			existingVehicle.setRatePerMonth(vehicleDto.getRatePerMonth());
			existingVehicle.setRatePerWeek(vehicleDto.getRatePerWeek());
			existingVehicle.setVehicleName(vehicleDto.getVehicleName());
			existingVehicle.setFuelType(FuelType.fromText(vehicleDto.getFuelType()));
			existingVehicle.setTransmission(VehicleTransmission.fromText(vehicleDto.getTransmission()));
			existingVehicle.setVehicleDoors(vehicleDto.getVehicleDoors());
			existingVehicle.setVehicleSeats(vehicleDto.getVehicleSeats());
			
			if (vehicleDto.getVehicleType() != null) {
                vehicleType.setVehicleType(VehicleTypeEnum.fromText(vehicleDto.getVehicleType()));
                existingVehicle.setVehicleType(vehicleType);
                existingVehicle.setVehicleStatus(VehicleStatus.AVAILABLE);
            }
			
			upload(files);
			existingVehicle.setVehicleImage(files.getOriginalFilename());
			
			vehicleTypeRepository.save(vehicleType);
			vehicleRepository.save(existingVehicle);
			
		}
		
	}
	
	public void upload(MultipartFile file) {
    	try {
    		Path copyLocation = Paths
    				.get(baseFilePath + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
    		Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
    		
    	}catch(Exception e) {
    		
    	}
    }

    public List<Vehicle> availableVehicle(LocalDateTime pickUpdate) {

        List<Reservation> reservationList = reservationRepository.findActivatedReservations(ACTIVATED);

        List<Vehicle> availableVehicle = vehicleRepository.findAll();

        if (!availableVehicle.isEmpty()) {

            for (Reservation reservation : reservationList) {

                List<Vehicle> vehicleList = reservation.getVehicleList();

                if (!vehicleList.isEmpty()) {

                    for (Vehicle vehicle : vehicleList) {

                        LocalDateTime reserveDueDate = reservation.getReturnDate();

                        if (pickUpdate.isBefore(reserveDueDate)) {
                            availableVehicle.remove(vehicle);
                        }

                    }
                }
            }
        }
        return availableVehicle;
    }

    public List<VehicleDao> getAvailableVehicleDto(List<Vehicle> vehicleList) {

        if (!vehicleList.isEmpty()) {
            List<VehicleDao> availableVehicleDtoList = new ArrayList<>();

            for (Vehicle vehicle : vehicleList) {
                VehicleDao vehicleDao = new VehicleDao();
                vehicleDao.setVehicleId(vehicle.getVehicleId());
                vehicleDao.setVehicleType(vehicle.getVehicleType().getVehicleType().toString().replace("_", " "));
                if (vehicle.getVehicleName() != null) {
                    vehicleDao.setVehicleName(vehicle.getVehicleName());
                }
                if (vehicle.getFuelType() != null) {
                    vehicleDao.setFuelType(vehicle.getFuelType().toString());
                }
                if (vehicle.getVehicleDoors() != 0) {
                    vehicleDao.setVehicleDoors(vehicle.getVehicleDoors());
                }
                if (vehicle.getVehicleSeats() != 0) {
                    vehicleDao.setVehicleSeats(vehicle.getVehicleSeats());
                }
                if (vehicle.getTransmission() != null) {
                    vehicleDao.setTransmission(vehicle.getTransmission().toString());
                }
                if (vehicle.getPricePerDay() != 0.0) {
                    vehicleDao.setPricePerDay(vehicle.getPricePerDay());
                }
                if (vehicle.getRatePerMonth() != 0.0) {
                    vehicleDao.setRatePerMonth(vehicle.getRatePerMonth());
                }
                if (vehicle.getRatePerWeek() != 0.0) {
                    vehicleDao.setRatePerWeek(vehicle.getRatePerWeek());
                }
                if (vehicle.getVehicleImage() != null) {
                    vehicleDao.setVehicleImage(vehicle.getVehicleImage());
                }

                availableVehicleDtoList.add(vehicleDao);
            }

            return availableVehicleDtoList;
        }

        return null;
    }


}




















