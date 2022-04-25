package com.sachini.booking.dao;

import java.util.List;

public class RequestReservationDao {

    private Integer userId;

    private Integer vehicleId;

    private List<EquipmentDao> equipmentList;

    private String status;

    private String requestDuration;

    private String reserveDate;

    private String returnDate;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public List<EquipmentDao> getEquipmentList() {
        return equipmentList;
    }

    public void setEquipmentList(List<EquipmentDao> equipmentList) {
        this.equipmentList = equipmentList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestDuration() {
        return requestDuration;
    }

    public void setRequestDuration(String requestDuration) {
        this.requestDuration = requestDuration;
    }

    public String getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(String reserveDate) {
        this.reserveDate = reserveDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
}
