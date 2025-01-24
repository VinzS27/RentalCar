package com.si2001.rentalcar.DAO;

import com.si2001.rentalcar.model.Reservation;

import java.util.List;

public interface ReservationDAO {

    Reservation getReservationById(int id);

    List<Reservation> getAllReservations();

    Reservation getReservationStatus(String status);

    void saveReservation(Reservation reservation);

    void deleteReservationById(int id);

}
