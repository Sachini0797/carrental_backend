package com.sachini.booking.service;

import com.sachini.booking.dao.EquipmentDao;
import com.sachini.booking.dao.ReservationDao;
import com.sachini.booking.dao.UserDao;
import com.sachini.booking.enums.UserStatus;
import com.sachini.booking.enums.VehicleStatus;
import com.sachini.booking.enums.VehicleTypeEnum;
import com.sachini.booking.model.Equipment;
import com.sachini.booking.model.Reservation;
import com.sachini.booking.model.Users;
import com.sachini.booking.model.Vehicle;
import com.sachini.booking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    public static final String ACTIVATED = "ACTIVATED";
    public static final String NOT_FOUND = "NOT FOUND";
    public static final String COMPLETED = "COMPLETED";


    @Override
    public String addReservation(Integer userId, Integer vehicleId, List<EquipmentDao> equipmentList, String vehicleReserveDate, String vehicleReturnDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime reserveDate = LocalDateTime.parse(vehicleReserveDate.replace("T", " "), formatter);
        LocalDateTime returnDate = LocalDateTime.parse(vehicleReturnDate.replace("T", " "), formatter);

        if (checkVehicleType(vehicleId) == VehicleTypeEnum.NOTHING) {

            if (checkVehicleStatus(vehicleId, reserveDate, returnDate).equals(VehicleStatus.AVAILABLE)) {

                if (!equipmentList.isEmpty()) {
                    // proceed the booking
                    Reservation reservation = new Reservation();
                    reservation.setReserveDate(reserveDate);
                    reservation.setReturnDate(returnDate);

                    Optional<Users> userOpt = userRepository.findById(userId);
                    if (userOpt.isPresent()) {
                        Users users = userOpt.get();
                        reservation.setUser(users);
                    }

                    List<Vehicle> vehicleList = new ArrayList<>();
                    Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);
                    if (vehicleOpt.isPresent()) {
                        Vehicle vehicle = vehicleOpt.get();
                        vehicleList.add(vehicle);
                    }

                    reservation.setVehicleList(vehicleList);


                    List<Equipment> equipment = new ArrayList<>();
                    List<Reservation> reservationList = new ArrayList<>();
                    reservationList.add(reservation);

                    for (EquipmentDao equipmentDto : equipmentList) {

                        Optional<Equipment> equipmentOpt = equipmentRepository.findById(equipmentDto.getEquipmentId());
                        if (equipmentOpt.isPresent()) {
                            Equipment equipmentModel = equipmentOpt.get();
                            equipment.add(equipmentModel);
                            equipmentModel.setReserveEquipmentList(reservationList);
                            equipmentRepository.save(equipmentModel);

                        }

                    }

                    reservation.setEquipmentList(equipment);
                    reservation.setStatus(ACTIVATED);
                    reservationRepository.save(reservation);

                    return "Successfully Reserved A Vehicle";


                } else {
                    //proceed the booking without the equipment
                    Reservation reservation = new Reservation();
                    reservation.setReserveDate(reserveDate);
                    reservation.setReturnDate(returnDate);

                    Optional<Users> userOpt = userRepository.findById(userId);
                    if (userOpt.isPresent()) {
                        Users users = userOpt.get();
                        reservation.setUser(users);
                    }

                    List<Vehicle> vehicleList = new ArrayList<>();
                    Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);
                    if (vehicleOpt.isPresent()) {
                        Vehicle vehicle = vehicleOpt.get();
                        vehicleList.add(vehicle);
                    }
                    reservation.setStatus(ACTIVATED);
                    reservation.setVehicleList(vehicleList);
                    reservationRepository.save(reservation);

                    return "Successfully Reserved A Vehicle. No Equipments Added";
                }
            } else {
                return "Your Selected Vehicle Is Already Reserved";
            }

        } else {

            if (validateAge(userId).contentEquals("ALLOWED")) {

                if (checkVehicleStatus(vehicleId, reserveDate, returnDate).equals(VehicleStatus.AVAILABLE)) {

                    if (!equipmentList.isEmpty()) {
                        // proceed the booking
                        Reservation reservation = new Reservation();
                        reservation.setReserveDate(reserveDate);
                        reservation.setReturnDate(returnDate);

                        Optional<Users> userOpt = userRepository.findById(userId);
                        if (userOpt.isPresent()) {
                            Users users = userOpt.get();
                            reservation.setUser(users);
                        }

                        List<Vehicle> vehicleList = new ArrayList<>();
                        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);
                        if (vehicleOpt.isPresent()) {
                            Vehicle vehicle = vehicleOpt.get();
                            vehicleList.add(vehicle);
                        }

                        reservation.setVehicleList(vehicleList);

                        List<Equipment> equipment = new ArrayList<>();

                        for (EquipmentDao equipmentDto : equipmentList) {

                            Equipment equipmentModel = new Equipment();
                            equipmentModel.setEquipmentId(equipmentDto.getEquipmentId());
                            equipmentModel.setEquipmentName(equipmentDto.getEquipmentName());
                            equipmentModel.setEquipmentStatus(false);

                            equipment.add(equipmentModel);
                        }

                        reservation.setStatus(ACTIVATED);
                        reservation.setEquipmentList(equipment);
                        reservationRepository.save(reservation);

                        return "Successfully Reserved";
                    } else {
                        //proceed the booking without the equipment
                        Reservation reservation = new Reservation();
                        reservation.setReserveDate(reserveDate);
                        reservation.setReturnDate(returnDate);

                        Optional<Users> userOpt = userRepository.findById(userId);
                        if (userOpt.isPresent()) {
                            Users users = userOpt.get();
                            reservation.setUser(users);
                        }

                        List<Vehicle> vehicleList = new ArrayList<>();
                        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);
                        if (vehicleOpt.isPresent()) {
                            Vehicle vehicle = vehicleOpt.get();
                            vehicleList.add(vehicle);
                        }
                        reservation.setStatus(ACTIVATED);
                        reservation.setVehicleList(vehicleList);
                        reservationRepository.save(reservation);
                        
                        Optional<Users> userOpt2 = userRepository.findById(userId);
                        Users usersDomain = null;
                        if (userOpt.isPresent()) {
                            usersDomain = userOpt.get();

                            String email = usersDomain.getEmail();
                            sendEmailToUser(email);

                        }

                        return "Successfully Reserved A Vehicle. No Equipments Added";
                    }
                } else {
                    return "Your Selected Vehicle Is Already Reserved";
                }
            } else {
                return "This Vehicle Available For Age 18+ Customers only";
            }

        }
    }

    private String validateAge(Integer userId) {

        Optional<Users> userOpt = userRepository.findById(userId);

        if (userOpt.isPresent()) {
            Users users = userOpt.get();
            LocalDate userBirthYear = users.getDateOfBirth();
            LocalDate presentYear = LocalDate.now();
            Period period = Period.between(userBirthYear, presentYear);

            if (period.getYears() >= 18) {
                return "ALLOWED";
            } else {
                return "NOT ALLOWED";
            }
        }

        return NOT_FOUND;
    }

    @Override
    public List<ReservationDao> listMyReservation(UserDao userDto) {

        List<ReservationDao> myReservationList = new ArrayList<>();
        Integer userId = userDto.getUserId();
        String status = ACTIVATED;
        List<Reservation> reservationList = reservationRepository.findByUserId(userId, status);

        if (!reservationList.isEmpty()) {
            for (Reservation reservation : reservationList) {

                ReservationDao reservationDto = new ReservationDao();

                if (reservation.getReservationId() != 0) {
                    reservationDto.setReservationId(reservation.getReservationId());
                }
                if (!reservation.getVehicleList().isEmpty()) {
                    reservationDto.setVehicleName(reservation.getVehicleList().get(0).getVehicleName());
                }

                if (!reservation.getEquipmentList().isEmpty()) {
                    List<Equipment> equipmentList = reservation.getEquipmentList();
                    String equipment = " ";
                    for (Equipment equipmentModel : equipmentList) {
                        equipment = equipment + equipmentModel.getEquipmentName() + " ";
                    }
                    reservationDto.setEquipment(equipment);
                }

                if (reservation.getReserveDate().toString() != null) {

                    String input = reservation.getReserveDate().toString().replace("T", " ").replace("-", "/");
                    DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");

                    DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
                    Date date = null;
                    String output = null;
                    try {
                        date = df.parse(input);
                        output = outputformat.format(date);
                        reservationDto.setReserveDate(output);

                    } catch (ParseException pe) {
                        //catch
                    }

                }

                if (reservation.getReturnDate().toString() != null) {
                    String input = reservation.getReturnDate().toString().replace("T", " ").replace("-", "/");
                    DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");

                    DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
                    Date date = null;
                    String output = null;
                    try {
                        date = df.parse(input);
                        output = outputformat.format(date);
                        reservationDto.setReturnDate(output);

                    } catch (ParseException pe) {
                        //catch
                    }
                }

                if (!reservation.getReserveDate().toString().isEmpty() && !reservation.getReturnDate().toString().isEmpty()) {
                    long days;
                    long hours;
                    days = ChronoUnit.DAYS.between(reservation.getReserveDate(), reservation.getReturnDate());
                    if (days != 0) {
                        String durationAsDays = String.valueOf(days);
                        reservationDto.setRequestDuration("Days " + durationAsDays);
                    } else {
                        hours = ChronoUnit.HOURS.between(reservation.getReserveDate(), reservation.getReturnDate());
                        String durationAsHours = String.valueOf(hours);
                        reservationDto.setRequestDuration("Hours " + durationAsHours);
                    }

                }
                myReservationList.add(reservationDto);
            }
        }

        return myReservationList;
    }

    @Override
    public String cancelReservation(ReservationDao reservationDto) {

        if (reservationDto.getReservationId() != 0) {
            Optional<Reservation> reservationOpt = reservationRepository.findById(reservationDto.getReservationId());
            if (reservationOpt.isPresent()) {
                Reservation reservationDomain = reservationOpt.get();
                reservationDomain.setStatus("CANCELED");
                reservationRepository.save(reservationDomain);

                return "Reservation id-" + reservationDto.getReservationId().toString() + " Reservation Canceled";
            }

            return NOT_FOUND;
        }

        return null;
    }

    @Override
    public void deleteReservation(ReservationDao reservationDto) {

        if (reservationDto.getReservationId() != 0) {
            reservationRepository.deleteById(reservationDto.getReservationId());
        }
    }

    @Override
    public String completeReservation(ReservationDao reservationDto) {

        if (reservationDto.getReservationId() != 0) {
            Optional<Reservation> reservationOpt = reservationRepository.findById(reservationDto.getReservationId());
            if (reservationOpt.isPresent()) {
                Reservation reservationModel = reservationOpt.get();
                reservationModel.setStatus(COMPLETED);

                if (reservationModel.getUser() != null) {
                    Users usersModel = reservationModel.getUser();
                    usersModel.setUserStatus(UserStatus.LOYALCUSTOMER);
                    userRepository.save(usersModel);
                    sendCompleteEmailToUser(usersModel.getEmail());
                }
                reservationRepository.save(reservationModel);
                return "Reservation completed";
            }

        }
        return null;
    }

    @Override
    public List<ReservationDao> listReservation() {

        List<Reservation> reservationModelList = reservationRepository.findActivatedReservations(ACTIVATED);
        List<ReservationDao> reservationDtoList = new ArrayList<>();

        if (!reservationModelList.isEmpty()) {
            for (Reservation reservation : reservationModelList) {

                ReservationDao reservationDto = new ReservationDao();

                if (reservation.getReservationId() != 0) {
                    reservationDto.setReservationId(reservation.getReservationId());
                }
                if (!reservation.getVehicleList().isEmpty()) {
                    reservationDto.setVehicleName(reservation.getVehicleList().get(0).getVehicleName());
                }

                if (!reservation.getEquipmentList().isEmpty()) {
                    List<Equipment> equipmentList = reservation.getEquipmentList();
                    String equipments = " ";
                    for (Equipment equipment : equipmentList) {
                        equipments = equipments + equipment.getEquipmentName() + " ";
                    }
                    reservationDto.setEquipment(equipments);
                } else {
                    reservationDto.setEquipment("No Equipments Added");
                }

                if (reservation.getReserveDate().toString() != null) {

                    String input = reservation.getReserveDate().toString().replace("T", " ").replace("-", "/");
                    DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");

                    DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
                    Date date = null;
                    String output = null;
                    try {
                        date = df.parse(input);
                        output = outputformat.format(date);
                        reservationDto.setReserveDate(output);

                    } catch (ParseException pe) {
                        //catch
                    }

                }

                if (reservation.getReturnDate().toString() != null) {
                    String input = reservation.getReturnDate().toString().replace("T", " ").replace("-", "/");
                    DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");

                    DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
                    Date date = null;
                    String output = null;
                    try {
                        date = df.parse(input);
                        output = outputformat.format(date);
                        reservationDto.setReturnDate(output);

                    } catch (ParseException pe) {
                        //catch
                    }
                }

                if (!reservation.getReserveDate().toString().isEmpty() && !reservation.getReturnDate().toString().isEmpty()) {
                    long days;
                    long hours;
                    days = ChronoUnit.DAYS.between(reservation.getReserveDate(), reservation.getReturnDate());
                    if (days != 0) {
                        String durationAsDays = String.valueOf(days);
                        reservationDto.setRequestDuration("Days " + durationAsDays);
                    } else {
                        hours = ChronoUnit.HOURS.between(reservation.getReserveDate(), reservation.getReturnDate());
                        String durationAsHours = String.valueOf(hours);
                        reservationDto.setRequestDuration("Hours " + durationAsHours);
                    }

                }

                if (reservation.getUser().getUsername() != null) {
                    reservationDto.setUser(reservation.getUser().getUsername());
                }

                if (reservation.getUser().getUserId() != 0) {
                    reservationDto.setUserId(reservation.getUser().getUserId());
                }

                if (reservation.getStatus() != null) {
                    reservationDto.setStatus(reservation.getStatus());
                }

                reservationDtoList.add(reservationDto);
            }
        }

        return reservationDtoList;
    }

    private void sendEmailToUser(String sendEemail) {

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(sendEemail);
        email.setSubject("Car Rental 365");
        email.setText("Thanks for your valuable reservation. Hope you enjoy the moments with us."
                + " See you soon in next ride.");

        javaMailSender.send(email);
    }
    
    private void sendCompleteEmailToUser(String sendEemail) {

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(sendEemail);
        email.setSubject("Car Rental 365");
        email.setText("Your reservation has been successfully completed."
                + " Thank you for choose us for your valuable ride.");

        javaMailSender.send(email);
    }

    public VehicleTypeEnum checkVehicleType(Integer vehicleId) {

        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);
        if (vehicleOpt.isPresent()) {
            Vehicle vehicle = vehicleOpt.get();
            return vehicle.getVehicleType().getVehicleType();
        }

        return null;
    }

    public VehicleStatus checkVehicleStatus(Integer vehicleId, LocalDateTime vehicleReserveDate, LocalDateTime vehicleReturnDate) {

        List<Reservation> reservationList = reservationRepository.findActivatedReservations(ACTIVATED);

        boolean flag = true;

        if (!reservationList.isEmpty()) {

            for (Reservation reservation : reservationList) {

                List<Vehicle> vehiclesInReserve = reservation.getVehicleList();

                if (!vehiclesInReserve.isEmpty()) {

                    for (Vehicle vehicle : vehiclesInReserve) {

                        if (vehicle.getVehicleId().toString().contentEquals(vehicleId.toString())) {
                            //Vehicle is  booked

                            LocalDateTime vehicleReturnDateCurrentReserve = reservation.getReturnDate();
                            LocalDateTime vehicleReserveDateCurrentReserve = reservation.getReserveDate();

                            if (vehicleReserveDate.isBefore(vehicleReturnDateCurrentReserve) && vehicleReturnDate.isBefore(vehicleReserveDateCurrentReserve) ||
                                    vehicleReserveDate.isAfter(vehicleReturnDateCurrentReserve) && vehicleReturnDate.isAfter(vehicleReserveDateCurrentReserve)) {
                                flag = true;
                            } else {
                                flag = false;
                                break;
                            }

                        }

                    }

                    if (flag) {
                        return VehicleStatus.AVAILABLE;
                    } else {
                        return VehicleStatus.RESERVED;
                    }
                }

            }
        } else {

            Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);

            if (vehicleOpt.isPresent()) {

                Vehicle vehicle = vehicleOpt.get();

                if (vehicle.getVehicleStatus().equals(VehicleStatus.AVAILABLE)) {
                    return VehicleStatus.AVAILABLE;
                } else {
                    return VehicleStatus.RESERVED;
                }
            }

        }

        return null;
    }
}
