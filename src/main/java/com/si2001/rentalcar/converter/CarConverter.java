package com.si2001.rentalcar.converter;

import com.si2001.rentalcar.model.Car;
import com.si2001.rentalcar.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CarConverter implements Converter<Object, Car> {

    final CarService carService;

    public CarConverter(CarService carService) {
        this.carService = carService;
    }

    public Car convert(Object o) {
        return carService.getCarById((Integer) o);
    }
}
