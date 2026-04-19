package com.khairpur.view;

import com.khairpur.controller.BookingController;
import com.khairpur.model.Booking;
import com.khairpur.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.PrinterException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * User panel showing a passenger's own bookings with ticket generation.
 */
public class MyBookingsPanel extends JPanel {

    private final BookingController bookingController = new BookingController();
    private final User currentUser;
    private JTable table;
    private DefaultTableModel tableModel;

    private static final String[] COLUMNS = {
            "ID", "Route", "From", "To", "Type", "Journey Date", "Seats", "Fare (Rs.)", "Status"
    };

    public MyBookingsPanel(User user) {
        this.currentUser = user;
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("My Bookings - " + currentUser.getName());
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(27, 94, 32));
        add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(new Color(220, 220, 220));
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        JButton refreshBtn = createButton("Refresh", new Color(69, 90, 100));
        JButton cancelBtn = createButton("Cancel Booking", new Color(198, 40, 40));
        JButton ticketBtn = createButton("View Ticket", new Color(103, 58, 183));

        refreshBtn.addActionListener(e -> loadData());
        cancelBtn.addActionListener(e -> cancelSelectedBooking());
        ticketBtn.addActionListener(e -> showTicket());

        btnPanel.add(refreshBtn);
        btnPanel.add(cancelBtn);
        btnPanel.add(ticketBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    void loadData() {
        tableModel.setRowCount(0);
        List<Booking> bookings = bookingController.getBookingsByUser(currentUser.getId());
        for (Booking b : bookings) {
            tableModel.addRow(new Object[]{
                    b.getId(),
                    b.getRouteName(),
                    b.getFromLocation(),
                    b.getToLocation(),
                    b.getTransportType(),
                    b.getJourneyDate(),
                    b.getSeats(),
                    String.format("%.0f", b.getTotalFare()),
                    b.getStatus()
            });
        }
    }

    private int getSelectedBookingId() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a booking first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return -1;
        }
        return (int) tableModel.getValueAt(row, 0);
    }

    private void cancelSelectedBooking() {
        int id = getSelectedBookingId();
        if (id < 0) return;
        String status = (String) tableModel.getValueAt(table.getSelectedRow(), 8);
        if (!"Confirmed".equals(status)) {
            JOptionPane.showMessageDialog(this, "Only confirmed bookings can be cancelled.", "Cannot Cancel", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel this booking?",
                "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (bookingController.cancelBooking(id)) {
                JOptionPane.showMessageDialog(this, "Booking cancelled successfully.");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to cancel booking.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showTicket() {
        int id = getSelectedBookingId();
        if (id < 0) return;
        Booking booking = bookingController.getBookingById(id);
        if (booking == null) {
            JOptionPane.showMessageDialog(this, "Booking details not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        displayTicket(booking);
    }

    void displayTicket(Booking booking) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        String transportIcon = getTransportIcon(booking.getTransportType());

        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════════════════════╗\n");
        sb.append("║        KHAIRPUR MULTI-TRANSPORT MANAGEMENT SYSTEM        ║\n");
        sb.append("║                    BOOKING TICKET                        ║\n");
        sb.append("╠══════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║  Ticket No. : #%-42d ║%n", booking.getId()));
        sb.append(String.format("║  Status     : %-42s ║%n", booking.getStatus()));
        sb.append("╠══════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║  Passenger  : %-42s ║%n", booking.getUserName()));
        sb.append("╠══════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║  Transport  : [%s] %-39s ║%n", transportIcon, booking.getTransportType()));
        sb.append(String.format("║  Route      : %-42s ║%n", booking.getRouteName()));
        sb.append(String.format("║  From       : %-42s ║%n", booking.getFromLocation()));
        sb.append(String.format("║  To         : %-42s ║%n", booking.getToLocation()));
        sb.append("╠══════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║  Vehicle    : %-42s ║%n",
                booking.getVehicleNumber() != null ? booking.getVehicleNumber() : "N/A"));
        sb.append(String.format("║  Driver     : %-42s ║%n",
                booking.getDriverName() != null ? booking.getDriverName() : "N/A"));
        sb.append("╠══════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║  Journey    : %-42s ║%n", booking.getJourneyDate().format(fmt)));
        sb.append(String.format("║  Booked On  : %-42s ║%n", booking.getBookingDate().format(fmt)));
        sb.append("╠══════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║  Seats      : %-42d ║%n", booking.getSeats()));
        sb.append(String.format("║  Total Fare : Rs. %-39.0f ║%n", booking.getTotalFare()));
        sb.append("╚══════════════════════════════════════════════════════════╝\n");
        sb.append("\n  Thank you for choosing Khairpur Transport System!\n");
        sb.append("  Have a safe journey!\n");

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setBackground(new Color(255, 255, 240));

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JButton printBtn = new JButton("🖶 Print Ticket");
        printBtn.setBackground(new Color(13, 71, 161));
        printBtn.setForeground(Color.WHITE);
        printBtn.setFocusPainted(false);
        printBtn.addActionListener(e -> {
            try {
                textArea.print();
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(null, "Print failed: " + ex.getMessage());
            }
        });

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRow.add(printBtn);
        panel.add(btnRow, BorderLayout.SOUTH);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Booking Ticket #" + booking.getId(), true);
        dialog.add(panel);
        dialog.setSize(560, 480);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private String getTransportIcon(String type) {
        if (type == null) return "Bus";
        switch (type) {
            case "Car": return "Car";
            case "Bike": return "Bike";
            case "Airline": return "Airline";
            default: return "Bus";
        }
    }
}
