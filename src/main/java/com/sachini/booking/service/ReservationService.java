package com.sachini.booking.service;

import com.sachini.booking.dao.EquipmentDao;
import com.sachini.booking.dao.ReservationDao;
import com.sachini.booking.dao.UserDao;

import java.util.List;

public interface ReservationService {

    public String addReservation(Integer userId, Integer vehicleId, List<EquipmentDao> equipmentList, String vehicleReserveDate, String vehicleReturnDate);

    public List<ReservationDao> listMyReservation(UserDao userDto);

    public String cancelReservation(ReservationDao reservationDto);

    public void deleteReservation(ReservationDao reservationDto);

    public String completeReservation(ReservationDao reservationDto);

    public List<ReservationDao> listReservation();
}
