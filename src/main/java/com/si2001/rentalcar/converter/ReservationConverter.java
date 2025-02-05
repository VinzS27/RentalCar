package com.si2001.rentalcar.converter;

import com.si2001.rentalcar.model.Reservation;
import com.si2001.rentalcar.service.ReservationService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ReservationConverter implements Converter<Object, Reservation> {

    final ReservationService reservationService;

    public ReservationConverter(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public Reservation convert(Object o) {
        return reservationService.getReservationById((Integer) o);
    }
}