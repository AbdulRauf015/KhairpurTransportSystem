package com.khairpur.view;

import com.khairpur.controller.BookingController;
import com.khairpur.model.Booking;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.*;
import java.util.List;

/**
 * Admin panel displaying all bookings with passenger list printing functionality.
 */
public class BookingListPanel extends JPanel {

    private final BookingController bookingController = new BookingController();
    private JTable table;
    private DefaultTableModel tableModel;

    private static final String[] COLUMNS = {
            "ID", "Passenger", "Route", "From", "To", "Type",
            "Vehicle", "Driver", "Journey Date", "Seats", "Fare (Rs.)", "Status"
    };

    public BookingListPanel() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("All Bookings - Passenger Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(13, 71, 161));
        add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(new Color(220, 220, 220));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        JButton refreshBtn = createButton("Refresh", new Color(69, 90, 100));
        JButton completeBtn = createButton("Mark Complete", new Color(46, 125, 50));
        JButton cancelBtn = createButton("Cancel Booking", new Color(198, 40, 40));
        JButton passengerListBtn = createButton("Print Passenger List", new Color(103, 58, 183));

        refreshBtn.addActionListener(e -> loadData());
        completeBtn.addActionListener(e -> markComplete());
        cancelBtn.addActionListener(e -> cancelSelected());
        passengerListBtn.addActionListener(e -> showPassengerList());

        btnPanel.add(refreshBtn);
        btnPanel.add(completeBtn);
        btnPanel.add(cancelBtn);
        btnPanel.add(passengerListBtn);
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
        List<Booking> bookings = bookingController.getAllBookings();
        for (Booking b : bookings) {
            tableModel.addRow(new Object[]{
                    b.getId(),
                    b.getUserName(),
                    b.getRouteName(),
                    b.getFromLocation(),
                    b.getToLocation(),
                    b.getTransportType(),
                    b.getVehicleNumber() != null ? b.getVehicleNumber() : "-",
                    b.getDriverName() != null ? b.getDriverName() : "-",
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

    private void markComplete() {
        int id = getSelectedBookingId();
        if (id < 0) return;
        if (bookingController.completeBooking(id)) {
            JOptionPane.showMessageDialog(this, "Booking marked as completed.");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update booking.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelSelected() {
        int id = getSelectedBookingId();
        if (id < 0) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Cancel this booking?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (bookingController.cancelBooking(id)) {
                JOptionPane.showMessageDialog(this, "Booking cancelled.");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to cancel booking. It may already be cancelled.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showPassengerList() {
        List<Booking> bookings = bookingController.getAllBookings();
        if (bookings.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No bookings to display.", "Passenger List", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append("         KHAIRPUR MULTI-TRANSPORT MANAGEMENT SYSTEM\n");
        sb.append("                    PASSENGER LIST\n");
        sb.append("═══════════════════════════════════════════════════════════\n\n");
        sb.append(String.format("%-4s  %-18s  %-22s  %-12s  %-5s  %-12s%n",
                "ID", "Passenger", "Route", "Journey Date", "Seats", "Status"));
        sb.append("───────────────────────────────────────────────────────────\n");

        int totalSeats = 0;
        for (Booking b : bookings) {
            if (!"Cancelled".equals(b.getStatus())) {
                sb.append(String.format("%-4d  %-18s  %-22s  %-12s  %-5d  %-12s%n",
                        b.getId(),
                        truncate(b.getUserName(), 18),
                        truncate(b.getRouteName(), 22),
                        b.getJourneyDate(),
                        b.getSeats(),
                        b.getStatus()
                ));
                totalSeats += b.getSeats();
            }
        }
        sb.append("───────────────────────────────────────────────────────────\n");
        sb.append(String.format("Total Active Bookings: %d  |  Total Seats: %d%n",
                (int) bookings.stream().filter(b -> !"Cancelled".equals(b.getStatus())).count(),
                totalSeats));
        sb.append("═══════════════════════════════════════════════════════════\n");

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(680, 450));

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton printBtn = new JButton("🖶 Print");
        printBtn.setBackground(new Color(13, 71, 161));
        printBtn.setForeground(Color.WHITE);
        printBtn.setFocusPainted(false);
        printBtn.addActionListener(e -> {
            try {
                textArea.print();
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(null, "Print failed: " + ex.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRow.add(printBtn);
        panel.add(btnRow, BorderLayout.SOUTH);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Passenger List", true);
        dialog.add(panel);
        dialog.setSize(720, 530);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private String truncate(String s, int len) {
        if (s == null) return "-";
        return s.length() > len ? s.substring(0, len - 1) + "…" : s;
    }
}
