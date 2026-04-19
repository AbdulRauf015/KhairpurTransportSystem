package com.khairpur.controller;

import com.khairpur.model.Vehicle;
import com.khairpur.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller handling vehicle CRUD operations.
 */
public class VehicleController {

    /**
     * Adds a new vehicle to the database.
     *
     * @return true if the vehicle was added successfully
     */
    public boolean addVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles (vehicle_type, vehicle_number, capacity, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, vehicle.getVehicleType());
            ps.setString(2, vehicle.getVehicleNumber());
            ps.setInt(3, vehicle.getCapacity());
            ps.setString(4, vehicle.getStatus());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    vehicle.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Add vehicle error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Returns all vehicles in the system.
     */
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                vehicles.add(mapVehicle(rs));
            }
        } catch (SQLException e) {
            System.err.println("Get vehicles error: " + e.getMessage());
        }
        return vehicles;
    }

    /**
     * Returns vehicles filtered by type.
     */
    public List<Vehicle> getVehiclesByType(String vehicleType) {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles WHERE vehicle_type = ? AND status = 'Active' ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vehicleType);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                vehicles.add(mapVehicle(rs));
            }
        } catch (SQLException e) {
            System.err.println("Get vehicles by type error: " + e.getMessage());
        }
        return vehicles;
    }

    /**
     * Updates an existing vehicle record.
     *
     * @return true if the update was successful
     */
    public boolean updateVehicle(Vehicle vehicle) {
        String sql = "UPDATE vehicles SET vehicle_type = ?, vehicle_number = ?, capacity = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vehicle.getVehicleType());
            ps.setString(2, vehicle.getVehicleNumber());
            ps.setInt(3, vehicle.getCapacity());
            ps.setString(4, vehicle.getStatus());
            ps.setInt(5, vehicle.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update vehicle error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a vehicle by id.
     *
     * @return true if deletion was successful
     */
    public boolean deleteVehicle(int id) {
        String sql = "DELETE FROM vehicles WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Delete vehicle error: " + e.getMessage());
            return false;
        }
    }

    private Vehicle mapVehicle(ResultSet rs) throws SQLException {
        return new Vehicle(
                rs.getInt("id"),
                rs.getString("vehicle_type"),
                rs.getString("vehicle_number"),
                rs.getInt("capacity"),
                rs.getString("status")
        );
    }
}
