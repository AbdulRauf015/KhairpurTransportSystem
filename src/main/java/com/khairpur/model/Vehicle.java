package com.khairpur.model;

/**
 * Represents a vehicle in the transport system.
 */
public class Vehicle {
    private int id;
    private String vehicleType; // Bus, Car, Bike, Airline
    private String vehicleNumber;
    private int capacity;
    private String status; // Active, Inactive

    public Vehicle() {}

    public Vehicle(int id, String vehicleType, String vehicleNumber, int capacity, String status) {
        this.id = id;
        this.vehicleType = vehicleType;
        this.vehicleNumber = vehicleNumber;
        this.capacity = capacity;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() { return vehicleNumber + " (" + vehicleType + ")"; }
}
