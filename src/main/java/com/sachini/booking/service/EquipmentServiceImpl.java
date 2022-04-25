package com.sachini.booking.service;

import com.sachini.booking.dao.EquipmentDao;
import com.sachini.booking.dao.VehicleDao;
import com.sachini.booking.enums.EquipmentStatus;
import com.sachini.booking.model.Equipment;
import com.sachini.booking.model.Reservation;
import com.sachini.booking.model.Vehicle;
import com.sachini.booking.repository.EquipmentRepository;
import com.sachini.booking.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public static final String ACTIVATED = "ACTIVATED";
    
    @Value("${basefilepath}")
    private String baseFilePath;

    @Override
    public void addEquipment(MultipartFile files, EquipmentDao equipmentDao) {
        Equipment equipmentModel = new Equipment();

        if (equipmentDao.getEquipmentName() != null) {
            equipmentModel.setEquipmentName(equipmentDao.getEquipmentName());
        }
        if (equipmentDao.getDescription() != null) {
            equipmentModel.setDescription(equipmentDao.getDescription());
        }

        if (equipmentDao.getEquipmentImage() != null) {
            equipmentModel.setEquipmentImage(equipmentDao.getEquipmentImage());
        }

        upload(files);
        equipmentModel.setEquipmentImage(files.getOriginalFilename());
//        equipmentModel.setEquipmentStatus(true);

        equipmentRepository.save(equipmentModel);

    }
    

    @Override
    public List<EquipmentDao> listEquipment() {
        List<Equipment> equipmentModelList = equipmentRepository.findAll();
        List<EquipmentDao> equipmentDtoList = new ArrayList<>();

        if (!equipmentModelList.isEmpty()) {

            for (Equipment equipment : equipmentModelList) {
                EquipmentDao equipmentDto = new EquipmentDao();

                if (equipment.getEquipmentId() != 0) {
                    equipmentDto.setEquipmentId(equipment.getEquipmentId());
                }

                if (equipment.getEquipmentName() != null) {
                    equipmentDto.setEquipmentName(equipment.getEquipmentName());
                }

                if (equipment.getDescription() != null) {
                    equipmentDto.setDescription(equipment.getDescription());
                } else {
                    equipmentDto.setDescription("No description");
                }
                
                if (equipment.getEquipmentImage() != null) {
                    equipmentDto.setEquipmentImage(equipment.getEquipmentImage());
                }

                if (equipment.isEquipmentStatus()) {
                    equipmentDto.setStatus("Available");
                } else {
                    equipmentDto.setStatus("Not Available");
                }

                equipmentDtoList.add(equipmentDto);
            }

        }
        return equipmentDtoList;
    }

    public List<Equipment> availableEquipment(LocalDateTime pickUpdate) {

        List<Reservation> reservationList = reservationRepository.findActivatedReservations(ACTIVATED);

        List<Equipment> availableEquipment = equipmentRepository.findAll();

        if (!availableEquipment.isEmpty()) {

            for (Reservation reservation : reservationList) {

                List<Equipment> equipmentList = reservation.getEquipmentList();

                if (!equipmentList.isEmpty()) {

                    for (Equipment equipment : equipmentList) {

                        LocalDateTime reserveDueDate = reservation.getReturnDate();

                        if (pickUpdate.isBefore(reserveDueDate)) {
                            availableEquipment.remove(equipment);
                        }

                    }
                }
            }
        }

        return availableEquipment;
    }

    public List<EquipmentDao> getAvailableEquipmentDto(List<Equipment> equipmentList) {

        if (!equipmentList.isEmpty()) {
            List<EquipmentDao> availableEquipmentDtoList = new ArrayList<>();

            for (Equipment equipment : equipmentList) {
                EquipmentDao equipmentDto = new EquipmentDao();
                equipmentDto.setEquipmentId(equipment.getEquipmentId());
                equipmentDto.setEquipmentName(equipment.getEquipmentName());

                availableEquipmentDtoList.add(equipmentDto);
            }

            return availableEquipmentDtoList;
        }

        return null;
    }
    
    
    @Override
	public void deleteEquipment(EquipmentDao equipmentDto) {
		// TODO Auto-generated method stub
		if(equipmentDto.getEquipmentId() != 0) {
			equipmentRepository.deleteById(equipmentDto.getEquipmentId());
		}
	}
    
    @Override
    public List<EquipmentDao> viewAllEquipment() {
    	
    	List<EquipmentDao> allEquipmentDtoList = new ArrayList<>();
        List<Equipment> allEquipmentList = equipmentRepository.findAll();
        
        for(Equipment equipment : allEquipmentList) {
        	
        	EquipmentDao equipmentDao = new EquipmentDao();
        	equipmentDao.setEquipmentId(equipment.getEquipmentId());
        	
        	if(equipment.getEquipmentName() != null) {
                equipmentDao.setEquipmentName(equipment.getEquipmentName());
            }
        	if(equipment.getDescription() != null) {
                equipmentDao.setDescription(equipment.getDescription());
            }
        	if(equipment.isEquipmentStatus()) {
                equipmentDao.setStatus("Available");
            }else {
            	equipmentDao.setStatus("Reserved");
            }
        	if(equipment.getEquipmentImage() != null) {
                equipmentDao.setEquipmentImage(equipment.getEquipmentImage());
            }
        	allEquipmentDtoList.add(equipmentDao);
        }
        return allEquipmentDtoList;
    }
    
    public void updateEquipment(MultipartFile files, EquipmentDao equipmentDto) {
    	
    	if(equipmentDto.getEquipmentId() !=0) {
    		Equipment existingEquipment = equipmentRepository.findById(equipmentDto.getEquipmentId()).orElse(null);
    		
    		existingEquipment.setEquipmentName(equipmentDto.getEquipmentName());
    		existingEquipment.setDescription(equipmentDto.getDescription());
    		
    		upload(files);
    		existingEquipment.setEquipmentImage(files.getOriginalFilename());
			
			equipmentRepository.save(existingEquipment);
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
}

