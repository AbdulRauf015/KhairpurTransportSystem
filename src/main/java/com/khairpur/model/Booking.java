package com.khairpur.model;

import java.time.LocalDate;

/**
 * Represents a booking made by a user.
 */
public class Booking {
    private int id;
    private int userId;
    private int routeId;
    private int vehicleId;
    private int driverId;
    private LocalDate bookingDate;
    private LocalDate journeyDate;
    private int seats;
    private double totalFare;
    private String status; // Confirmed, Cancelled, Completed

    // Denormalized fields for display
    private String userName;
    private String routeName;
    private String vehicleNumber;
    private String driverName;
    private String fromLocation;
    private String toLocation;
    private String transportType;

    public Booking() {}

    public Booking(int id, int userId, int routeId, int vehicleId, int driverId,
                   LocalDate bookingDate, LocalDate journeyDate, int seats,
                   double totalFare, String status) {
        this.id = id;
        this.userId = userId;
        this.routeId = routeId;
        this.vehicleId = vehicleId;
        this.driverId = driverId;
        this.bookingDate = bookingDate;
        this.journeyDate = journeyDate;
        this.seats = seats;
        this.totalFare = totalFare;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getRouteId() { return routeId; }
    public void setRouteId(int routeId) { this.routeId = routeId; }

    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

    public int getDriverId() { return driverId; }
    public void setDriverId(int driverId) { this.driverId = driverId; }

    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }

    public LocalDate getJourneyDate() { return journeyDate; }
    public void setJourneyDate(LocalDate journeyDate) { this.journeyDate = journeyDate; }

    public int getSeats() { return seats; }
    public void setSeats(int seats) { this.seats = seats; }

    public double getTotalFare() { return totalFare; }
    public void setTotalFare(double totalFare) { this.totalFare = totalFare; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }

    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public String getFromLocation() { return fromLocation; }
    public void setFromLocation(String fromLocation) { this.fromLocation = fromLocation; }

    public String getToLocation() { return toLocation; }
    public void setToLocation(String toLocation) { this.toLocation = toLocation; }

    public String getTransportType() { return transportType; }
    public void setTransportType(String transportType) { this.transportType = transportType; }
}
