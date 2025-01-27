package com.si2001.rentalcar.service;

import com.si2001.rentalcar.model.Car;
import jakarta.validation.Valid;

import java.util.List;

public interface CarService {

    Car getCarById(int id);

    List<Car> getAllCars();

    void saveCar(@Valid Car car);

    void updateCar(@Valid Car car);

    void deleteCarById(int id);
}
