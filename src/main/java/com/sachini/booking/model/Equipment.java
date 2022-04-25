package com.sachini.booking.model;

import javax.persistence.*;

import com.sachini.booking.enums.EquipmentStatus;

import java.util.List;

@Entity
@Table(name = "EQUIPMENT")
public class Equipment {

    @Id
    @Column(name = "equipment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer equipmentId;

    @Column(name = "equipment_name")
    private String equipmentName;

    @Column(name = "description")
    private String description;

    @Column(name = "equipmentImage")
    private String equipmentImage;

    @Column(name = "equipment_status")
    private boolean equipmentStatus;

    @ManyToMany(mappedBy = "equipmentList")
    private List<Reservation> reserveEquipmentList;

    public Integer getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Integer equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEquipmentImage() {
        return equipmentImage;
    }

    public void setEquipmentImage(String equipmentImage) {
        this.equipmentImage = equipmentImage;
    }

    

	public boolean isEquipmentStatus() {
		return equipmentStatus;
	}

	public void setEquipmentStatus(boolean equipmentStatus) {
		this.equipmentStatus = equipmentStatus;
	}

	public List<Reservation> getReserveEquipmentList() {
        return reserveEquipmentList;
    }

    public void setReserveEquipmentList(List<Reservation> reserveEquipmentList) {
        this.reserveEquipmentList = reserveEquipmentList;
    }
}
