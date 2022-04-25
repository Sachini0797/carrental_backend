package com.sachini.booking.model;

import com.sachini.booking.enums.VehicleTypeEnum;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "VEHICLE_TYPE")
public class VehicleType {

    @Id
    @Column(name = "vehicle_type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer vehicleTypeId;

    @OneToMany(mappedBy = "vehicleType")
    private List<Vehicle> vehicleList;

    private VehicleTypeEnum vehicleType;

    public Integer getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(Integer vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }

    public VehicleTypeEnum getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleTypeEnum vehicleType) {
        this.vehicleType = vehicleType;
    }
}
