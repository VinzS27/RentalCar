package com.si2001.rentalcar.DAO;

import com.si2001.rentalcar.model.Reservation;
import com.si2001.rentalcar.model.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ReservationDao")
public class ReservationDAOImpl extends AbstractDao<Integer, Reservation> implements ReservationDAO {

    public Reservation getReservationById(int id) {
        return getByKey(id);
    }

    public List<Reservation> getAllReservations() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Reservation> cq = cb.createQuery(Reservation.class);
        Root<Reservation> root = cq.from(Reservation.class);
        cq.select(root);
        return entityManager.createQuery(cq).getResultList();
    }


    public Reservation getReservationStatus(String status) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Reservation> cq = cb.createQuery(Reservation.class);
        Root<Reservation> root = cq.from(Reservation.class);
        cq.where(cb.like(root.get("status"), status));
        return entityManager.createQuery(cq).getSingleResult();
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
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Reservation> cq = cb.createQuery(Reservation.class);
        Root<Reservation> root = cq.from(Reservation.class);
        cq.where(cb.equal(root.get("id"), id));
        Reservation reservation = entityManager.createQuery(cq).getSingleResult();
        delete(reservation);
    }
}