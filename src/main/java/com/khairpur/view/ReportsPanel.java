package com.khairpur.view;

import com.khairpur.controller.BookingController;
import com.khairpur.controller.DriverController;
import com.khairpur.controller.RouteController;
import com.khairpur.controller.VehicleController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Admin reports panel showing booking statistics and revenue.
 */
public class ReportsPanel extends JPanel {

    private final BookingController bookingController = new BookingController();
    private final RouteController routeController = new RouteController();
    private final VehicleController vehicleController = new VehicleController();
    private final DriverController driverController = new DriverController();

    private JLabel totalBookingsLabel, confirmedLabel, cancelledLabel, completedLabel;
    private JLabel totalRevenueLabel, totalRoutesLabel, totalVehiclesLabel, totalDriversLabel;
    private JTextArea breakdownArea;

    public ReportsPanel() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("System Reports & Statistics");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(13, 71, 161));
        add(title, BorderLayout.NORTH);

        JPanel content = new JPanel(new GridLayout(1, 2, 15, 0));

        // Left: Stats cards
        JPanel statsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        statsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(13, 71, 161)), "Key Metrics"));

        totalBookingsLabel = createStatLabel("0", "Total Bookings", new Color(13, 71, 161));
        confirmedLabel = createStatLabel("0", "Confirmed", new Color(46, 125, 50));
        cancelledLabel = createStatLabel("0", "Cancelled", new Color(198, 40, 40));
        completedLabel = createStatLabel("0", "Completed", new Color(103, 58, 183));
        totalRevenueLabel = createStatLabel("0", "Revenue (Rs.)", new Color(230, 81, 0));
        totalRoutesLabel = createStatLabel("0", "Routes", new Color(0, 96, 100));
        totalVehiclesLabel = createStatLabel("0", "Vehicles", new Color(74, 20, 140));
        totalDriversLabel = createStatLabel("0", "Drivers", new Color(27, 94, 32));

        statsPanel.add(totalBookingsLabel);
        statsPanel.add(confirmedLabel);
        statsPanel.add(cancelledLabel);
        statsPanel.add(completedLabel);
        statsPanel.add(totalRevenueLabel);
        statsPanel.add(totalRoutesLabel);
        statsPanel.add(totalVehiclesLabel);
        statsPanel.add(totalDriversLabel);

        // Right: Breakdown by type
        JPanel breakdownPanel = new JPanel(new BorderLayout(5, 5));
        breakdownPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(13, 71, 161)), "Bookings by Transport Type"));
        breakdownArea = new JTextArea();
        breakdownArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        breakdownArea.setEditable(false);
        breakdownArea.setBackground(new Color(250, 250, 255));
        breakdownPanel.add(new JScrollPane(breakdownArea), BorderLayout.CENTER);

        content.add(statsPanel);
        content.add(breakdownPanel);
        add(content, BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Refresh Reports");
        refreshBtn.setBackground(new Color(13, 71, 161));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        refreshBtn.addActionListener(e -> loadData());
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(refreshBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private JLabel createStatLabel(String value, String caption, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(245, 248, 255));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.brighter(), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        JLabel val = new JLabel(value, SwingConstants.CENTER);
        val.setFont(new Font("Segoe UI", Font.BOLD, 26));
        val.setForeground(color);

        JLabel cap = new JLabel(caption, SwingConstants.CENTER);
        cap.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cap.setForeground(Color.GRAY);

        card.add(val, BorderLayout.CENTER);
        card.add(cap, BorderLayout.SOUTH);

        // Store the value label reference on the card using client property
        card.putClientProperty("valueLabel", val);

        // We use a wrapper label so we can return a label and update it later
        JLabel wrapper = new JLabel() {
            @Override
            public void setText(String text) {
                super.setText(text);
                val.setText(text);
            }
        };
        wrapper.add(card);
        // Actually return the card wrapped, but we need to track val label
        // Simple approach: return val label directly and add card to parent
        return val;
    }

    void loadData() {
        int[] stats = bookingController.getBookingStats();
        totalBookingsLabel.setText(String.valueOf(stats[0]));
        confirmedLabel.setText(String.valueOf(stats[1]));
        cancelledLabel.setText(String.valueOf(stats[2]));
        completedLabel.setText(String.valueOf(stats[3]));

        double revenue = bookingController.getTotalRevenue();
        totalRevenueLabel.setText(String.format("%.0f", revenue));
        totalRoutesLabel.setText(String.valueOf(routeController.getAllRoutes().size()));
        totalVehiclesLabel.setText(String.valueOf(vehicleController.getAllVehicles().size()));
        totalDriversLabel.setText(String.valueOf(driverController.getAllDrivers().size()));

        // Transport type breakdown
        List<Object[]> breakdown = bookingController.getBookingsByTransportType();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-12s  %8s  %14s%n", "Type", "Bookings", "Revenue (Rs.)"));
        sb.append("─".repeat(38)).append("\n");
        for (Object[] row : breakdown) {
            sb.append(String.format("%-12s  %8d  %14.0f%n",
                    row[0], (int) (Integer) row[1], (double) (Double) row[2]));
        }
        if (breakdown.isEmpty()) sb.append("No booking data available.\n");
        breakdownArea.setText(sb.toString());
    }
}
