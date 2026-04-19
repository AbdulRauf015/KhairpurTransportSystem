package com.khairpur.controller;

import com.khairpur.model.Driver;
import com.khairpur.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller handling driver CRUD operations.
 */
public class DriverController {

    /**
     * Adds a new driver to the database.
     *
     * @return true if the driver was added successfully
     */
    public boolean addDriver(Driver driver) {
        String sql = "INSERT INTO drivers (name, phone, license_number, vehicle_type) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, driver.getName());
            ps.setString(2, driver.getPhone());
            ps.setString(3, driver.getLicenseNumber());
            ps.setString(4, driver.getVehicleType());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    driver.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Add driver error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Returns all drivers in the system.
     */
    public List<Driver> getAllDrivers() {
        List<Driver> drivers = new ArrayList<>();
        String sql = "SELECT * FROM drivers ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                drivers.add(mapDriver(rs));
            }
        } catch (SQLException e) {
            System.err.println("Get drivers error: " + e.getMessage());
        }
        return drivers;
    }

    /**
     * Returns drivers filtered by vehicle type.
     */
    public List<Driver> getDriversByType(String vehicleType) {
        List<Driver> drivers = new ArrayList<>();
        String sql = "SELECT * FROM drivers WHERE vehicle_type = ? ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vehicleType);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                drivers.add(mapDriver(rs));
            }
        } catch (SQLException e) {
            System.err.println("Get drivers by type error: " + e.getMessage());
        }
        return drivers;
    }

    /**
     * Updates an existing driver record.
     *
     * @return true if the update was successful
     */
    public boolean updateDriver(Driver driver) {
        String sql = "UPDATE drivers SET name = ?, phone = ?, license_number = ?, vehicle_type = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, driver.getName());
            ps.setString(2, driver.getPhone());
            ps.setString(3, driver.getLicenseNumber());
            ps.setString(4, driver.getVehicleType());
            ps.setInt(5, driver.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update driver error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a driver by id.
     *
     * @return true if deletion was successful
     */
    public boolean deleteDriver(int id) {
        String sql = "DELETE FROM drivers WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Delete driver error: " + e.getMessage());
            return false;
        }
    }

    private Driver mapDriver(ResultSet rs) throws SQLException {
        return new Driver(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("phone"),
                rs.getString("license_number"),
                rs.getString("vehicle_type")
        );
    }
}
