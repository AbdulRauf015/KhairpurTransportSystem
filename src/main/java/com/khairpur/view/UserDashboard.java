package com.khairpur.view;

import com.khairpur.controller.BookingController;
import com.khairpur.controller.DriverController;
import com.khairpur.controller.RouteController;
import com.khairpur.controller.VehicleController;
import com.khairpur.model.Booking;
import com.khairpur.model.Driver;
import com.khairpur.model.TransportRoute;
import com.khairpur.model.User;
import com.khairpur.model.Vehicle;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Main window for regular users. Allows booking a ticket and viewing existing bookings.
 */
public class UserDashboard extends JFrame {

    private final User currentUser;
    private final RouteController routeController = new RouteController();
    private final VehicleController vehicleController = new VehicleController();
    private final DriverController driverController = new DriverController();
    private final BookingController bookingController = new BookingController();

    private MyBookingsPanel myBookingsPanel;

    // Booking form fields
    private JComboBox<String> transportTypeBox;
    private JComboBox<TransportRoute> routeBox;
    private JComboBox<Vehicle> vehicleBox;
    private JComboBox<Driver> driverBox;
    private JTextField journeyDateField;
    private JSpinner seatsSpinner;
    private JLabel fareLabel;
    private JLabel totalFareLabel;

    public UserDashboard(User user) {
        this.currentUser = user;
        initComponents();
    }

    private void initComponents() {
        setTitle("Khairpur Transport System - User Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 640);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(27, 94, 32));
        headerPanel.setPreferredSize(new Dimension(0, 60));

        JLabel titleLabel = new JLabel("  🚌 Khairpur Multi-Transport Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        rightHeader.setOpaque(false);
        JLabel userLabel = new JLabel("Welcome, " + currentUser.getName() + "  ");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userLabel.setForeground(new Color(200, 240, 200));
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(198, 40, 40));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.addActionListener(e -> logout());
        rightHeader.add(userLabel);
        rightHeader.add(logoutBtn);
        headerPanel.add(rightHeader, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        tabbedPane.addTab("🎫 Book a Ticket", buildBookingPanel());
        myBookingsPanel = new MyBookingsPanel(currentUser);
        tabbedPane.addTab("📋 My Bookings", myBookingsPanel);

        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 1) {
                myBookingsPanel.loadData();
            }
        });

        add(tabbedPane, BorderLayout.CENTER);

        // Status bar
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 3));
        statusBar.setBackground(new Color(232, 245, 233));
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        JLabel statusLabel = new JLabel("Khairpur Transport Management System v1.0 | User Portal");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(Color.DARK_GRAY);
        statusBar.add(statusLabel);
        add(statusBar, BorderLayout.SOUTH);
    }

    private JPanel buildBookingPanel() {
        JPanel outer = new JPanel(new BorderLayout(15, 15));
        outer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Left side: booking form
        JPanel formCard = new JPanel(new BorderLayout(0, 10));
        formCard.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(27, 94, 32)), "New Ticket Booking"));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(7, 5, 7, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Transport type
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        form.add(boldLabel("Transport Type:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        transportTypeBox = new JComboBox<>(new String[]{"Bus", "Car", "Bike", "Airline"});
        transportTypeBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        form.add(transportTypeBox, gbc);

        // Route
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        form.add(boldLabel("Route:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        routeBox = new JComboBox<>();
        routeBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        form.add(routeBox, gbc);

        // Vehicle
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        form.add(boldLabel("Vehicle:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        vehicleBox = new JComboBox<>();
        vehicleBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        form.add(vehicleBox, gbc);

        // Driver
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        form.add(boldLabel("Driver:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        driverBox = new JComboBox<>();
        driverBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        form.add(driverBox, gbc);

        // Journey date
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        form.add(boldLabel("Journey Date:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        journeyDateField = new JTextField(
                LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), 15);
        journeyDateField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        journeyDateField.setToolTipText("Format: yyyy-MM-dd");
        form.add(journeyDateField, gbc);

        // Seats
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        form.add(boldLabel("Seats:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        seatsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        seatsSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        form.add(seatsSpinner, gbc);

        // Fare info
        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0;
        form.add(boldLabel("Fare per Seat:"), gbc);
        gbc.gridx = 1;
        fareLabel = new JLabel("Rs. 0.00");
        fareLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        form.add(fareLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 7; gbc.weightx = 0;
        form.add(boldLabel("Total Fare:"), gbc);
        gbc.gridx = 1;
        totalFareLabel = new JLabel("Rs. 0.00");
        totalFareLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        totalFareLabel.setForeground(new Color(27, 94, 32));
        form.add(totalFareLabel, gbc);

        // Book button
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        JButton bookBtn = new JButton("🎫 Book Ticket");
        bookBtn.setBackground(new Color(27, 94, 32));
        bookBtn.setForeground(Color.WHITE);
        bookBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bookBtn.setFocusPainted(false);
        bookBtn.setPreferredSize(new Dimension(200, 40));
        bookBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        form.add(bookBtn, gbc);

        formCard.add(form, BorderLayout.CENTER);
        outer.add(formCard, BorderLayout.WEST);

        // Right: info panel with available routes
        JPanel infoCard = new JPanel(new BorderLayout(0, 10));
        infoCard.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(27, 94, 32)), "Available Routes"));

        JTextArea routeInfo = new JTextArea();
        routeInfo.setFont(new Font("Monospaced", Font.PLAIN, 12));
        routeInfo.setEditable(false);
        routeInfo.setBackground(new Color(249, 255, 249));
        infoCard.add(new JScrollPane(routeInfo), BorderLayout.CENTER);
        outer.add(infoCard, BorderLayout.CENTER);

        // Wire up events
        transportTypeBox.addActionListener(e -> {
            String type = (String) transportTypeBox.getSelectedItem();
            loadRoutesForType(type);
            loadVehiclesForType(type);
            loadDriversForType(type);
            updateRouteInfo(routeInfo, type);
            updateFareDisplay();
        });

        routeBox.addActionListener(e -> updateFareDisplay());
        seatsSpinner.addChangeListener(e -> updateFareDisplay());
        bookBtn.addActionListener(e -> bookTicket());

        // Initial load
        String initialType = (String) transportTypeBox.getSelectedItem();
        loadRoutesForType(initialType);
        loadVehiclesForType(initialType);
        loadDriversForType(initialType);
        updateRouteInfo(routeInfo, initialType);
        updateFareDisplay();

        return outer;
    }

    private JLabel boldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return lbl;
    }

    private void loadRoutesForType(String type) {
        routeBox.removeAllItems();
        List<TransportRoute> routes = routeController.getRoutesByType(type);
        for (TransportRoute r : routes) {
            routeBox.addItem(r);
        }
    }

    private void loadVehiclesForType(String type) {
        vehicleBox.removeAllItems();
        vehicleBox.addItem(null); // Allow no vehicle selected
        List<Vehicle> vehicles = vehicleController.getVehiclesByType(type);
        for (Vehicle v : vehicles) {
            vehicleBox.addItem(v);
        }
    }

    private void loadDriversForType(String type) {
        driverBox.removeAllItems();
        driverBox.addItem(null); // Allow no driver selected
        List<Driver> drivers = driverController.getDriversByType(type);
        for (Driver d : drivers) {
            driverBox.addItem(d);
        }
    }

    private void updateFareDisplay() {
        TransportRoute route = (TransportRoute) routeBox.getSelectedItem();
        if (route == null) {
            fareLabel.setText("Rs. 0.00");
            totalFareLabel.setText("Rs. 0.00");
            return;
        }
        int seats = (int) seatsSpinner.getValue();
        double fare = route.getFare();
        double total = fare * seats;
        fareLabel.setText(String.format("Rs. %.2f", fare));
        totalFareLabel.setText(String.format("Rs. %.2f", total));
    }

    private void updateRouteInfo(JTextArea area, String type) {
        List<TransportRoute> routes = routeController.getRoutesByType(type);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-28s  %-8s  %s%n", "Route", "Dist(km)", "Fare(Rs.)"));
        sb.append("─".repeat(50)).append("\n");
        for (TransportRoute r : routes) {
            sb.append(String.format("%-28s  %-8.0f  %.0f%n",
                    r.getFromLocation() + " → " + r.getToLocation(),
                    r.getDistance(), r.getFare()));
        }
        if (routes.isEmpty()) sb.append("No routes available for " + type + ".\n");
        area.setText(sb.toString());
    }

    private void bookTicket() {
        TransportRoute route = (TransportRoute) routeBox.getSelectedItem();
        if (route == null) {
            JOptionPane.showMessageDialog(this, "Please select a route.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String dateText = journeyDateField.getText().trim();
        LocalDate journeyDate;
        try {
            journeyDate = LocalDate.parse(dateText);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Journey date must be in yyyy-MM-dd format.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (journeyDate.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(this, "Journey date cannot be in the past.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int seats = (int) seatsSpinner.getValue();
        double totalFare = route.getFare() * seats;

        Vehicle selectedVehicle = (Vehicle) vehicleBox.getSelectedItem();
        Driver selectedDriver = (Driver) driverBox.getSelectedItem();

        // Confirm booking
        String confirm = String.format(
                "Confirm booking?\n\nRoute: %s\nFrom: %s → To: %s\nJourney: %s\nSeats: %d\nTotal Fare: Rs. %.2f",
                route.getRouteName(), route.getFromLocation(), route.getToLocation(),
                journeyDate, seats, totalFare
        );
        int choice = JOptionPane.showConfirmDialog(this, confirm, "Confirm Booking", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) return;

        Booking booking = new Booking();
        booking.setUserId(currentUser.getId());
        booking.setRouteId(route.getId());
        booking.setVehicleId(selectedVehicle != null ? selectedVehicle.getId() : 0);
        booking.setDriverId(selectedDriver != null ? selectedDriver.getId() : 0);
        booking.setBookingDate(LocalDate.now());
        booking.setJourneyDate(journeyDate);
        booking.setSeats(seats);
        booking.setTotalFare(totalFare);
        booking.setStatus("Confirmed");

        int bookingId = bookingController.createBooking(booking);
        if (bookingId > 0) {
            booking.setUserName(currentUser.getName());
            booking.setRouteName(route.getRouteName());
            booking.setFromLocation(route.getFromLocation());
            booking.setToLocation(route.getToLocation());
            booking.setTransportType(route.getTransportType());
            if (selectedVehicle != null) booking.setVehicleNumber(selectedVehicle.getVehicleNumber());
            if (selectedDriver != null) booking.setDriverName(selectedDriver.getName());

            JOptionPane.showMessageDialog(this,
                    "Booking confirmed! Ticket No. #" + bookingId,
                    "Booking Successful", JOptionPane.INFORMATION_MESSAGE);

            // Show ticket immediately
            myBookingsPanel.displayTicket(booking);
            myBookingsPanel.loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Booking failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginForm().setVisible(true);
        }
    }
}
