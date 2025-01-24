package com.si2001.rentalcar.service;

import com.si2001.rentalcar.model.Reservation;

import java.util.List;


public interface ReservationService {

    Reservation getReservationById(int id);

    Reservation getAllStatus(String status);

    List<Reservation> getAllReservations();
}
