package com.sachini.booking.repository;

import com.sachini.booking.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query(value = "SELECT * FROM Reservation WHERE user_id=? && status=?", nativeQuery = true)
    public List<Reservation> findByUserId(Integer userId, String status);

    @Query(value = "SELECT * FROM Reservation WHERE status=? ORDER BY return_date DESC ", nativeQuery = true)
    public List<Reservation> findActivatedReservations(String status);
}
