package com.khairpur.controller;

import com.khairpur.model.TransportRoute;
import com.khairpur.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller handling transport route CRUD operations.
 */
public class RouteController {

    /**
     * Adds a new transport route.
     *
     * @return true if the route was added successfully
     */
    public boolean addRoute(TransportRoute route) {
        String sql = "INSERT INTO routes (route_name, from_location, to_location, distance, fare, transport_type) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, route.getRouteName());
            ps.setString(2, route.getFromLocation());
            ps.setString(3, route.getToLocation());
            ps.setDouble(4, route.getDistance());
            ps.setDouble(5, route.getFare());
            ps.setString(6, route.getTransportType());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    route.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Add route error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Returns all transport routes.
     */
    public List<TransportRoute> getAllRoutes() {
        List<TransportRoute> routes = new ArrayList<>();
        String sql = "SELECT * FROM routes ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                routes.add(mapRoute(rs));
            }
        } catch (SQLException e) {
            System.err.println("Get routes error: " + e.getMessage());
        }
        return routes;
    }

    /**
     * Returns routes filtered by transport type.
     */
    public List<TransportRoute> getRoutesByType(String transportType) {
        List<TransportRoute> routes = new ArrayList<>();
        String sql = "SELECT * FROM routes WHERE transport_type = ? ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, transportType);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                routes.add(mapRoute(rs));
            }
        } catch (SQLException e) {
            System.err.println("Get routes by type error: " + e.getMessage());
        }
        return routes;
    }

    /**
     * Updates an existing route record.
     *
     * @return true if the update was successful
     */
    public boolean updateRoute(TransportRoute route) {
        String sql = "UPDATE routes SET route_name = ?, from_location = ?, to_location = ?, distance = ?, fare = ?, transport_type = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, route.getRouteName());
            ps.setString(2, route.getFromLocation());
            ps.setString(3, route.getToLocation());
            ps.setDouble(4, route.getDistance());
            ps.setDouble(5, route.getFare());
            ps.setString(6, route.getTransportType());
            ps.setInt(7, route.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update route error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a route by id.
     *
     * @return true if deletion was successful
     */
    public boolean deleteRoute(int id) {
        String sql = "DELETE FROM routes WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Delete route error: " + e.getMessage());
            return false;
        }
    }

    private TransportRoute mapRoute(ResultSet rs) throws SQLException {
        return new TransportRoute(
                rs.getInt("id"),
                rs.getString("route_name"),
                rs.getString("from_location"),
                rs.getString("to_location"),
                rs.getDouble("distance"),
                rs.getDouble("fare"),
                rs.getString("transport_type")
        );
    }
}
