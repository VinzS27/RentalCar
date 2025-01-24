package com.si2001.rentalcar.converter;

import com.si2001.rentalcar.model.Car;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CarConverter implements Converter<String, Car> {

    @Override
    public Car convert(String id) {
        Car car = new Car();
        car.setId(Integer.parseInt(id));
        return car;
    }
}
