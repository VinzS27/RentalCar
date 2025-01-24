package com.si2001.rentalcar.service;

import com.si2001.rentalcar.DAO.CarDAOImpl;
import com.si2001.rentalcar.model.Car;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("CarService")
@Transactional
public class CarServiceImpl implements CarService {

    final CarDAOImpl dao;

    @Autowired
    public CarServiceImpl(CarDAOImpl dao) {
        this.dao = dao;
    }

    public Car getCarById(int id) {
        return dao.getCarById(id);
    }

    public List<Car> getAllCars() {
        return dao.getAllCars();
    }
}
