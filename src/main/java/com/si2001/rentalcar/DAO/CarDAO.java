package com.si2001.rentalcar.DAO;

import com.si2001.rentalcar.model.Car;

import java.util.List;

public interface CarDAO {

    Car getCarById(int id);

    void saveCar(Car car);

    void deleteCarById(int id);

    List<Car> getAllCars();

    void updateCar(Car car);
}
