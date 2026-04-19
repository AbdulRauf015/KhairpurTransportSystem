package com.khairpur.controller;

import com.khairpur.model.Booking;
import com.khairpur.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller handling booking creation, retrieval, and status management.
 */
public class BookingController {

    /**
     * Creates a new booking.
     *
     * @return the generated booking id, or -1 on failure
     */
    public int createBooking(Booking booking) {
        String sql = "INSERT INTO bookings (user_id, route_id, vehicle_id, driver_id, booking_date, journey_date, seats, total_fare, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, booking.getUserId());
            ps.setInt(2, booking.getRouteId());
            if (booking.getVehicleId() > 0) {
                ps.setInt(3, booking.getVehicleId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            if (booking.getDriverId() > 0) {
                ps.setInt(4, booking.getDriverId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setDate(5, Date.valueOf(booking.getBookingDate()));
            ps.setDate(6, Date.valueOf(booking.getJourneyDate()));
            ps.setInt(7, booking.getSeats());
            ps.setDouble(8, booking.getTotalFare());
            ps.setString(9, "Confirmed");
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    int id = keys.getInt(1);
                    booking.setId(id);
                    return id;
                }
            }
        } catch (SQLException e) {
            System.err.println("Create booking error: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Returns all bookings with joined user, route, vehicle, and driver data.
     */
    public List<Booking> getAllBookings() {
        return fetchBookings("SELECT b.*, u.name AS user_name, r.route_name, r.from_location, r.to_location, r.transport_type, v.vehicle_number, d.name AS driver_name " +
                "FROM bookings b " +
                "LEFT JOIN users u ON b.user_id = u.id " +
                "LEFT JOIN routes r ON b.route_id = r.id " +
                "LEFT JOIN vehicles v ON b.vehicle_id = v.id " +
                "LEFT JOIN drivers d ON b.driver_id = d.id " +
                "ORDER BY b.id DESC", null);
    }

    /**
     * Returns bookings for a specific user.
     */
    public List<Booking> getBookingsByUser(int userId) {
        return fetchBookings("SELECT b.*, u.name AS user_name, r.route_name, r.from_location, r.to_location, r.transport_type, v.vehicle_number, d.name AS driver_name " +
                "FROM bookings b " +
                "LEFT JOIN users u ON b.user_id = u.id " +
                "LEFT JOIN routes r ON b.route_id = r.id " +
                "LEFT JOIN vehicles v ON b.vehicle_id = v.id " +
                "LEFT JOIN drivers d ON b.driver_id = d.id " +
                "WHERE b.user_id = ? ORDER BY b.id DESC", userId);
    }

    /**
     * Cancels a booking by updating its status.
     *
     * @return true if cancellation was successful
     */
    public boolean cancelBooking(int bookingId) {
        String sql = "UPDATE bookings SET status = 'Cancelled' WHERE id = ? AND status = 'Confirmed'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Cancel booking error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Marks a booking as Completed.
     *
     * @return true if the update was successful
     */
    public boolean completeBooking(int bookingId) {
        String sql = "UPDATE bookings SET status = 'Completed' WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Complete booking error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Returns a single booking by id with full details.
     */
    public Booking getBookingById(int bookingId) {
        List<Booking> results = fetchBookings("SELECT b.*, u.name AS user_name, r.route_name, r.from_location, r.to_location, r.transport_type, v.vehicle_number, d.name AS driver_name " +
                "FROM bookings b " +
                "LEFT JOIN users u ON b.user_id = u.id " +
                "LEFT JOIN routes r ON b.route_id = r.id " +
                "LEFT JOIN vehicles v ON b.vehicle_id = v.id " +
                "LEFT JOIN drivers d ON b.driver_id = d.id " +
                "WHERE b.id = ?", bookingId);
        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * Returns booking statistics for the reports panel.
     */
    public int[] getBookingStats() {
        int[] stats = {0, 0, 0, 0}; // total, confirmed, cancelled, completed
        String sql = "SELECT " +
                "COUNT(*) AS total, " +
                "SUM(CASE WHEN status = 'Confirmed' THEN 1 ELSE 0 END) AS confirmed, " +
                "SUM(CASE WHEN status = 'Cancelled' THEN 1 ELSE 0 END) AS cancelled, " +
                "SUM(CASE WHEN status = 'Completed' THEN 1 ELSE 0 END) AS completed " +
                "FROM bookings";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                stats[0] = rs.getInt("total");
                stats[1] = rs.getInt("confirmed");
                stats[2] = rs.getInt("cancelled");
                stats[3] = rs.getInt("completed");
            }
        } catch (SQLException e) {
            System.err.println("Get booking stats error: " + e.getMessage());
        }
        return stats;
    }

    /**
     * Returns total revenue from confirmed and completed bookings.
     */
    public double getTotalRevenue() {
        String sql = "SELECT SUM(total_fare) AS revenue FROM bookings WHERE status != 'Cancelled'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("revenue");
            }
        } catch (SQLException e) {
            System.err.println("Get revenue error: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Returns booking counts grouped by transport type.
     */
    public List<Object[]> getBookingsByTransportType() {
        List<Object[]> results = new ArrayList<>();
        String sql = "SELECT r.transport_type, COUNT(*) AS cnt, SUM(b.total_fare) AS revenue " +
                "FROM bookings b LEFT JOIN routes r ON b.route_id = r.id " +
                "GROUP BY r.transport_type ORDER BY cnt DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                results.add(new Object[]{
                        rs.getString("transport_type"),
                        rs.getInt("cnt"),
                        rs.getDouble("revenue")
                });
            }
        } catch (SQLException e) {
            System.err.println("Get bookings by type error: " + e.getMessage());
        }
        return results;
    }

    private List<Booking> fetchBookings(String sql, Integer param) {
        List<Booking> bookings = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            if (param != null) {
                ps.setInt(1, param);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Booking b = new Booking(
                        rs.getInt("b.id"),
                        rs.getInt("b.user_id"),
                        rs.getInt("b.route_id"),
                        rs.getInt("b.vehicle_id"),
                        rs.getInt("b.driver_id"),
                        toLocalDate(rs.getDate("b.booking_date")),
                        toLocalDate(rs.getDate("b.journey_date")),
                        rs.getInt("b.seats"),
                        rs.getDouble("b.total_fare"),
                        rs.getString("b.status")
                );
                b.setUserName(rs.getString("user_name"));
                b.setRouteName(rs.getString("route_name"));
                b.setFromLocation(rs.getString("from_location"));
                b.setToLocation(rs.getString("to_location"));
                b.setTransportType(rs.getString("transport_type"));
                b.setVehicleNumber(rs.getString("vehicle_number"));
                b.setDriverName(rs.getString("driver_name"));
                bookings.add(b);
            }
        } catch (SQLException e) {
            System.err.println("Fetch bookings error: " + e.getMessage());
        }
        return bookings;
    }

    private LocalDate toLocalDate(Date date) {
        return date != null ? date.toLocalDate() : LocalDate.now();
    }
}
