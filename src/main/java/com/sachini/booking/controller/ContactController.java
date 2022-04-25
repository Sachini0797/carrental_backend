package com.sachini.booking.controller;//package com.example.anton.controller;
//
//import ch.qos.logback.classic.Logger;
//import com.example.anton.dto.RequestReservationDto;
//import com.example.anton.service.EquipmentServiceImpl;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@CrossOrigin
//@RestController
//@RequestMapping("/api/contact")
//public class ContactController {
//
//    @Autowired
//    private ContactServiceImpl equipmentService;
//
//    private final Logger logger = (Logger) LoggerFactory.getLogger(ReservationController.class);
//
//    @RequestMapping(value = "/add_reservation", method = RequestMethod.POST)
//    public String addReservation(@RequestBody RequestReservationDto requestReservationDtoDto) {
//
//        return reservationService.addReservation(requestReservationDtoDto.getUserId(),
//                requestReservationDtoDto.getVehicleId(),
//                requestReservationDtoDto.getEquipmentList(),
//                requestReservationDtoDto.getReserveDate(),
//                requestReservationDtoDto.getReturnDate());
//
//    }
//}
