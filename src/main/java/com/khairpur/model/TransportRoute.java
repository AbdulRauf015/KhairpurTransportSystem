package com.khairpur.model;

/**
 * Represents a transport route in the system.
 */
public class TransportRoute {
    private int id;
    private String routeName;
    private String fromLocation;
    private String toLocation;
    private double distance;
    private double fare;
    private String transportType; // Bus, Car, Bike, Airline

    public TransportRoute() {}

    public TransportRoute(int id, String routeName, String fromLocation,
                          String toLocation, double distance, double fare, String transportType) {
        this.id = id;
        this.routeName = routeName;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.distance = distance;
        this.fare = fare;
        this.transportType = transportType;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }

    public String getFromLocation() { return fromLocation; }
    public void setFromLocation(String fromLocation) { this.fromLocation = fromLocation; }

    public String getToLocation() { return toLocation; }
    public void setToLocation(String toLocation) { this.toLocation = toLocation; }

    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }

    public double getFare() { return fare; }
    public void setFare(double fare) { this.fare = fare; }

    public String getTransportType() { return transportType; }
    public void setTransportType(String transportType) { this.transportType = transportType; }

    @Override
    public String toString() { return routeName + " (" + fromLocation + " → " + toLocation + ")"; }
}
