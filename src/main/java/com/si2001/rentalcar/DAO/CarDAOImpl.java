package com.si2001.rentalcar.DAO;

import com.si2001.rentalcar.model.Car;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("CarDao")
public class CarDAOImpl extends AbstractDao<Integer, Car> implements CarDAO {

    public Car getCarById(int id) {
        return getByKey(id);
    }

    public List<Car> getAllCars() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Car> cq = cb.createQuery(Car.class);
        Root<Car> root = cq.from(Car.class);
        cq.select(root);
        return entityManager.createQuery(cq).getResultList();
    }


    public void updateCar(Car car) {
        update(car);
    }

    public void saveCar(Car car) {
        persist(car);
    }

    public void deleteCarById(int id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Car> cq = cb.createQuery(Car.class);
        Root<Car> root = cq.from(Car.class);
        cq.where(cb.equal(root.get("id"), id));
        Car car = entityManager.createQuery(cq).getSingleResult();
        delete(car);
    }
}
