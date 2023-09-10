package com.example.rentease;

public class CarModel {
    String name,capacity,rent,car_image,car_id,doors,seats,transmission,hybrid,fuel,company;

    public String getDoors() {
        return doors;
    }

    public void setDoors(String doors) {
        this.doors = doors;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getHybrid() {
        return hybrid;
    }

    public void setHybrid(String hybrid) {
        this.hybrid = hybrid;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getName() {
        return name;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getCar_image() {
        return car_image;
    }

    public void setCar_image(String car_image) {
        this.car_image = car_image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }




    public CarModel() {
    }

    public CarModel( String name, String capacity, String rent, String car_image,String car_id){
        this.capacity=capacity;
        this.rent=rent;
        this.name=name;
        this.car_image=car_image;
        this.car_id = car_id;
    }
}
