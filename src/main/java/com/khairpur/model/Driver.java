package com.khairpur.model;

/**
 * Represents a driver in the transport system.
 */
public class Driver extends Person {
    private String licenseNumber;
    private String vehicleType; // Bus, Car, Bike, Airline

    public Driver() {}

    public Driver(int id, String name, String phone, String licenseNumber, String vehicleType) {
        super(id, name, phone);
        this.licenseNumber = licenseNumber;
        this.vehicleType = vehicleType;
    }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    @Override
    public String toString() { return name + " [" + vehicleType + "]"; }
}
