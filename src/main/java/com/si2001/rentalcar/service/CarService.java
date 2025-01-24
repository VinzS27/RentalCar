package com.si2001.rentalcar.service;

import com.si2001.rentalcar.model.Car;

import java.util.List;

public interface CarService {

    Car getCarById(int id);

    List<Car> getAllCars();
}
