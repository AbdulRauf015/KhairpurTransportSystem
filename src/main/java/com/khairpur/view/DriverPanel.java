package com.khairpur.view;

import com.khairpur.controller.DriverController;
import com.khairpur.model.Driver;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Admin panel for managing drivers (CRUD operations).
 */
public class DriverPanel extends JPanel {

    private final DriverController driverController = new DriverController();
    private JTable table;
    private DefaultTableModel tableModel;

    private static final String[] COLUMNS = {"ID", "Name", "Phone", "License Number", "Vehicle Type"};
    private static final String[] TRANSPORT_TYPES = {"Bus", "Car", "Bike", "Airline"};

    public DriverPanel() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Driver Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(13, 71, 161));
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
        JButton addBtn = createButton("Add Driver", new Color(46, 125, 50));
        JButton editBtn = createButton("Edit Driver", new Color(13, 71, 161));
        JButton deleteBtn = createButton("Delete Driver", new Color(198, 40, 40));
        JButton refreshBtn = createButton("Refresh", new Color(69, 90, 100));

        addBtn.addActionListener(e -> showDriverDialog(null));
        editBtn.addActionListener(e -> editSelectedDriver());
        deleteBtn.addActionListener(e -> deleteSelectedDriver());
        refreshBtn.addActionListener(e -> loadData());

        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(refreshBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(115, 32));
        return btn;
    }

    void loadData() {
        tableModel.setRowCount(0);
        List<Driver> drivers = driverController.getAllDrivers();
        for (Driver d : drivers) {
            tableModel.addRow(new Object[]{
                    d.getId(), d.getName(), d.getPhone(), d.getLicenseNumber(), d.getVehicleType()
            });
        }
    }

    private Driver getSelectedDriver() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a driver first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);
        String phone = (String) tableModel.getValueAt(row, 2);
        String license = (String) tableModel.getValueAt(row, 3);
        String type = (String) tableModel.getValueAt(row, 4);
        return new Driver(id, name, phone, license, type);
    }

    private void editSelectedDriver() {
        Driver d = getSelectedDriver();
        if (d != null) showDriverDialog(d);
    }

    private void deleteSelectedDriver() {
        Driver d = getSelectedDriver();
        if (d == null) return;
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete driver '" + d.getName() + "'?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (driverController.deleteDriver(d.getId())) {
                JOptionPane.showMessageDialog(this, "Driver deleted successfully.");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete driver.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showDriverDialog(Driver existing) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                existing == null ? "Add New Driver" : "Edit Driver", true);
        dialog.setSize(380, 260);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 5, 6, 5);

        JTextField nameField = new JTextField(15);
        JTextField phoneField = new JTextField(15);
        JTextField licenseField = new JTextField(15);
        JComboBox<String> typeBox = new JComboBox<>(TRANSPORT_TYPES);

        if (existing != null) {
            nameField.setText(existing.getName());
            phoneField.setText(existing.getPhone());
            licenseField.setText(existing.getLicenseNumber());
            typeBox.setSelectedItem(existing.getVehicleType());
        }

        String[] labels = {"Full Name:", "Phone:", "License Number:", "Vehicle Type:"};
        Component[] fields = {nameField, phoneField, licenseField, typeBox};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            panel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            panel.add(fields[i], gbc);
        }

        JButton saveBtn = new JButton(existing == null ? "Add Driver" : "Save Changes");
        saveBtn.setBackground(new Color(13, 71, 161));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        gbc.gridx = 0; gbc.gridy = labels.length; gbc.gridwidth = 2;
        panel.add(saveBtn, gbc);

        dialog.add(panel);

        saveBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String license = licenseField.getText().trim();
            String type = (String) typeBox.getSelectedItem();

            if (name.isEmpty() || license.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Name and License Number are required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Driver driver = new Driver(
                    existing == null ? 0 : existing.getId(),
                    name, phone, license, type
            );

            boolean success = existing == null
                    ? driverController.addDriver(driver)
                    : driverController.updateDriver(driver);

            if (success) {
                JOptionPane.showMessageDialog(dialog, existing == null ? "Driver added!" : "Driver updated!");
                loadData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Operation failed. License number may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }
}
