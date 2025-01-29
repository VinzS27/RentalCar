package com.si2001.rentalcar.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @Column(name = "availability", nullable = false)
    private boolean availability;

    public Car() {}

    public Car(int id, String model, String brand, int year, boolean availability) {
        this.id = id;
        this.model = model;
        this.brand = brand;
        this.year = year;
        this.availability = availability;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean getAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", brand='" + brand + '\'' +
                ", year=" + year +
                ", licensePlate='" + licensePlate + '\'' +
                ", availability='" + availability + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return id == car.id && year == car.year && Objects.equals(model, car.model) && Objects.equals(brand, car.brand) && Objects.equals(licensePlate, car.licensePlate) && Objects.equals(availability, car.availability);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, model, brand, year, licensePlate, availability);
    }
}
