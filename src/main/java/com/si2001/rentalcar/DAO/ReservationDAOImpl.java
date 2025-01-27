package com.si2001.rentalcar.DAO;

import com.si2001.rentalcar.model.Reservation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ReservationDao")
public class ReservationDAOImpl extends AbstractDao<Integer, Reservation> implements ReservationDAO {

    public Reservation getReservationById(int id) {
        return getByKey(id);
    }

    public List<Reservation> getAllReservations() {
        return getEntityManager()
                .createQuery("SELECT r FROM Reservation r", Reservation.class)
                .getResultList();
    }

    public Reservation getReservationStatus(String status) {
        return (Reservation) getEntityManager()
                .createQuery("SELECT r FROM Reservation r WHERE r.status LIKE :status")
                .setParameter("status", status)
                .getSingleResult();
    }

    public List<Reservation> getReservationsByUsername(String username) {
        return getEntityManager()
                .createQuery("SELECT r FROM Reservation r WHERE r.user.username LIKE :username", Reservation.class)
                .setParameter("username", username)
                .getResultList();
    }

    public void updateReservation(Reservation reservation) {
        update(reservation);
    }

    public void saveReservation(Reservation reservation) {
        persist(reservation);
    }

    public void deleteReservationById(int id) {
        Reservation reservation = (Reservation) getEntityManager()
                .createQuery("SELECT u FROM Reservation u WHERE u.id = :id")
                .setParameter("id", id)
                .getSingleResult();
        delete(reservation);
    }
}