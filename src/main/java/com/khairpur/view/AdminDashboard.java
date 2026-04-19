package com.khairpur.view;

import com.khairpur.model.User;

import javax.swing.*;
import java.awt.*;

/**
 * Main window for administrators. Contains tabbed panels for managing
 * routes, vehicles, drivers, bookings, and viewing reports.
 */
public class AdminDashboard extends JFrame {

    private final User adminUser;
    private RoutePanel routePanel;
    private VehiclePanel vehiclePanel;
    private DriverPanel driverPanel;
    private BookingListPanel bookingListPanel;
    private ReportsPanel reportsPanel;

    public AdminDashboard(User adminUser) {
        this.adminUser = adminUser;
        initComponents();
    }

    private void initComponents() {
        setTitle("Khairpur Transport System - Admin Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1050, 680);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top header bar
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(13, 71, 161));
        headerPanel.setPreferredSize(new Dimension(0, 60));

        JLabel titleLabel = new JLabel("  🚌 Khairpur Multi-Transport Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        rightHeader.setOpaque(false);
        JLabel userLabel = new JLabel("Admin: " + adminUser.getName() + "  ");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userLabel.setForeground(new Color(200, 220, 255));
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(198, 40, 40));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> logout());
        rightHeader.add(userLabel);
        rightHeader.add(logoutBtn);
        headerPanel.add(rightHeader, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setTabPlacement(JTabbedPane.TOP);

        routePanel = new RoutePanel();
        vehiclePanel = new VehiclePanel();
        driverPanel = new DriverPanel();
        bookingListPanel = new BookingListPanel();
        reportsPanel = new ReportsPanel();

        tabbedPane.addTab("🗺 Routes", routePanel);
        tabbedPane.addTab("🚗 Vehicles", vehiclePanel);
        tabbedPane.addTab("👤 Drivers", driverPanel);
        tabbedPane.addTab("📋 Bookings", bookingListPanel);
        tabbedPane.addTab("📊 Reports", reportsPanel);

        // Refresh data when a tab is selected
        tabbedPane.addChangeListener(e -> {
            int idx = tabbedPane.getSelectedIndex();
            if (idx == 0) routePanel.loadData();
            else if (idx == 1) vehiclePanel.loadData();
            else if (idx == 2) driverPanel.loadData();
            else if (idx == 3) bookingListPanel.loadData();
            else if (idx == 4) reportsPanel.loadData();
        });

        add(tabbedPane, BorderLayout.CENTER);

        // Status bar
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 3));
        statusBar.setBackground(new Color(232, 234, 246));
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        JLabel statusLabel = new JLabel("Connected | Khairpur Transport Management System v1.0");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(Color.DARK_GRAY);
        statusBar.add(statusLabel);
        add(statusBar, BorderLayout.SOUTH);
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
