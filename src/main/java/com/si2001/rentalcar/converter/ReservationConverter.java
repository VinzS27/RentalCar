package com.si2001.rentalcar.converter;

import com.si2001.rentalcar.model.Reservation;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ReservationConverter implements Converter<String, Reservation> {

    @Override
    public Reservation convert(String id) {
        Reservation reservation = new Reservation();
        reservation.setId(Integer.parseInt(id));
        return reservation;
    }
}