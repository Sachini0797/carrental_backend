package com.sachini.booking.service;


import com.sachini.booking.dao.EquipmentDao;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface EquipmentService {

    public void addEquipment(MultipartFile files, EquipmentDao equipmentDao);

    public List<EquipmentDao> listEquipment();
    
    public void deleteEquipment(EquipmentDao equipmentDto);
    
    public void updateEquipment(MultipartFile files, EquipmentDao equipmentDto);
    
    List<EquipmentDao> viewAllEquipment();
}
