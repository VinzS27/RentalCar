package com.si2001.rentalcar.service;

import com.si2001.rentalcar.model.Reservation;
import jakarta.validation.Valid;

import java.util.List;


public interface ReservationService {

    Reservation getReservationById(int id);

    Reservation getAllStatus(String status);

    List<Reservation> getAllReservations();

    List<Reservation> getReservationsByUsername(String username);

    void saveReservation(@Valid Reservation reservation);

    void updateReservation(@Valid Reservation reservation);

    void deleteReservationById(int id);

    void approveReservation(int id);

    void declineReservation(int id);
}
