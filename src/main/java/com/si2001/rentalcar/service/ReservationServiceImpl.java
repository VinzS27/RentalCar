package com.si2001.rentalcar.service;

import com.si2001.rentalcar.DAO.ReservationDAOImpl;
import com.si2001.rentalcar.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("ReservationService")
@Transactional
public class ReservationServiceImpl implements ReservationService {

    final ReservationDAOImpl dao;

    @Autowired
    public ReservationServiceImpl(ReservationDAOImpl dao) {
        this.dao = dao;
    }

    public Reservation getReservationById(int id) {
        return dao.getReservationById(id);
    }

    public Reservation getAllStatus(String status) {
        return dao.getReservationStatus(status);
    }

    public List<Reservation> getAllReservations() {
        return dao.getAllReservations();
    }

    public List<Reservation> getReservationsByUsername(String username) {
        return dao.getReservationsByUsername(username);
    }

    public void saveReservation(Reservation reservation) {
        dao.saveReservation(reservation);
    }

    public void updateReservation(Reservation reservation) {
        dao.updateReservation(reservation);
    }

    public void deleteReservationById(int id) {
        dao.deleteReservationById(id);
    }

    public void approveReservation(int id) {
        Reservation reservation = dao.getReservationById(id);
        reservation.setStatus("approvato");
        dao.saveReservation(reservation);
    }

    public void declineReservation(int id) {
        Reservation reservation = dao.getReservationById(id);
        reservation.setStatus("non approvato");
        dao.saveReservation(reservation);
    }
}
