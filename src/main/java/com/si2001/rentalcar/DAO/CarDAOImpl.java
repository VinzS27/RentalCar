package com.si2001.rentalcar.DAO;

import com.si2001.rentalcar.model.Car;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("CarDao")
public class CarDAOImpl extends AbstractDao<Integer, Car> implements CarDAO {

    public Car getCarById(int id) {
        return getByKey(id);
    }

    public List<Car> getAllCars() {
        return getEntityManager()
                .createQuery("SELECT c FROM Car c", Car.class)
                .getResultList();
    }

    public void updateCar(Car car) {
        update(car);
    }

    public void saveCar(Car car) {
        persist(car);
    }

    public void deleteCarById(int id) {
        Car car = (Car) getEntityManager()
                .createQuery("SELECT c FROM Car c WHERE c.id = :id")
                .setParameter("id", id)
                .getSingleResult();
        delete(car);
    }
}
