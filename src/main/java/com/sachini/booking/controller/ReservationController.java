package com.sachini.booking.controller;

import ch.qos.logback.classic.Logger;
import com.sachini.booking.dao.RequestReservationDao;
import com.sachini.booking.dao.ReservationDao;
import com.sachini.booking.dao.UserDao;
import com.sachini.booking.service.EquipmentServiceImpl;
import com.sachini.booking.service.ReservationServiceImpl;
import com.sachini.booking.service.VehicleServiceImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class ReservationController {

    @Autowired
    private ReservationServiceImpl reservationService;

    @Autowired
    private VehicleServiceImpl vehicleService;

    @Autowired
    private EquipmentServiceImpl equipmentService;

    private final Logger logger = (Logger) LoggerFactory.getLogger(ReservationController.class);

    @RequestMapping(value = "/reservation/add_reservation", method = RequestMethod.POST)
    public String addReservation(@RequestBody RequestReservationDao requestReservationDtoDto) {

        return reservationService.addReservation(requestReservationDtoDto.getUserId(),
                requestReservationDtoDto.getVehicleId(),
                requestReservationDtoDto.getEquipmentList(),
                requestReservationDtoDto.getReserveDate(),
                requestReservationDtoDto.getReturnDate());

    }

    @RequestMapping(value = "/reservation/list_my_reservation", method = RequestMethod.POST)
    public List<ReservationDao> listMyReservation(@RequestBody UserDao userDto) {
        if (userDto.getUserId() != 0) {
            return reservationService.listMyReservation(userDto);
        }
        return null;
    }

    @RequestMapping(value = "/reservation/cancel_reservation", method = RequestMethod.POST)
    public String cancelReservation(@RequestBody ReservationDao reservationDto) {

        if (reservationDto.getReservationId() != 0) {

            return reservationService.cancelReservation(reservationDto);
        }

        return "Something went wrong, failed cancellation";
    }

    @RequestMapping(value = "/admin/complete_reservation", method = RequestMethod.POST)
    public String completeReservation(@RequestBody ReservationDao reservationDto) {

        if (reservationDto.getReservationId() != 0) {
            return reservationService.completeReservation(reservationDto);
        } else {
            return "Something went wrong, Reservation not Found";
        }

    }

    @RequestMapping(value = "/admin/delete_reservation", method = RequestMethod.POST)
    public void deleteReservation(@RequestBody ReservationDao reservationDto) {

        if (reservationDto.getReservationId() != 0) {
            reservationService.deleteReservation(reservationDto);
        }

    }

    @RequestMapping(value = "/admin/list_reservation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReservationDao> listReservation() {
        return reservationService.listReservation();
    }


}
