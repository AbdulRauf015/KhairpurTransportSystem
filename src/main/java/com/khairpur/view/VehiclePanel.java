package com.khairpur.view;

import com.khairpur.controller.VehicleController;
import com.khairpur.model.Vehicle;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Admin panel for managing vehicles (CRUD operations).
 */
public class VehiclePanel extends JPanel {

    private final VehicleController vehicleController = new VehicleController();
    private JTable table;
    private DefaultTableModel tableModel;

    private static final String[] COLUMNS = {"ID", "Type", "Vehicle Number", "Capacity", "Status"};
    private static final String[] TRANSPORT_TYPES = {"Bus", "Car", "Bike", "Airline"};
    private static final String[] STATUSES = {"Active", "Inactive"};

    public VehiclePanel() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Vehicle Fleet Management");
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
        JButton addBtn = createButton("Add Vehicle", new Color(46, 125, 50));
        JButton editBtn = createButton("Edit Vehicle", new Color(13, 71, 161));
        JButton deleteBtn = createButton("Delete Vehicle", new Color(198, 40, 40));
        JButton refreshBtn = createButton("Refresh", new Color(69, 90, 100));

        addBtn.addActionListener(e -> showVehicleDialog(null));
        editBtn.addActionListener(e -> editSelectedVehicle());
        deleteBtn.addActionListener(e -> deleteSelectedVehicle());
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
        btn.setPreferredSize(new Dimension(130, 32));
        return btn;
    }

    void loadData() {
        tableModel.setRowCount(0);
        List<Vehicle> vehicles = vehicleController.getAllVehicles();
        for (Vehicle v : vehicles) {
            tableModel.addRow(new Object[]{
                    v.getId(), v.getVehicleType(), v.getVehicleNumber(), v.getCapacity(), v.getStatus()
            });
        }
    }

    private Vehicle getSelectedVehicle() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a vehicle first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        String type = (String) tableModel.getValueAt(row, 1);
        String number = (String) tableModel.getValueAt(row, 2);
        int capacity = (int) tableModel.getValueAt(row, 3);
        String status = (String) tableModel.getValueAt(row, 4);
        return new Vehicle(id, type, number, capacity, status);
    }

    private void editSelectedVehicle() {
        Vehicle v = getSelectedVehicle();
        if (v != null) showVehicleDialog(v);
    }

    private void deleteSelectedVehicle() {
        Vehicle v = getSelectedVehicle();
        if (v == null) return;
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete vehicle '" + v.getVehicleNumber() + "'?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (vehicleController.deleteVehicle(v.getId())) {
                JOptionPane.showMessageDialog(this, "Vehicle deleted successfully.");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete vehicle.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showVehicleDialog(Vehicle existing) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                existing == null ? "Add New Vehicle" : "Edit Vehicle", true);
        dialog.setSize(380, 270);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 5, 6, 5);

        JComboBox<String> typeBox = new JComboBox<>(TRANSPORT_TYPES);
        JTextField numberField = new JTextField(15);
        JTextField capacityField = new JTextField(15);
        JComboBox<String> statusBox = new JComboBox<>(STATUSES);

        if (existing != null) {
            typeBox.setSelectedItem(existing.getVehicleType());
            numberField.setText(existing.getVehicleNumber());
            capacityField.setText(String.valueOf(existing.getCapacity()));
            statusBox.setSelectedItem(existing.getStatus());
        }

        String[] labels = {"Vehicle Type:", "Vehicle Number:", "Capacity:", "Status:"};
        Component[] fields = {typeBox, numberField, capacityField, statusBox};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            panel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            panel.add(fields[i], gbc);
        }

        JButton saveBtn = new JButton(existing == null ? "Add Vehicle" : "Save Changes");
        saveBtn.setBackground(new Color(13, 71, 161));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        gbc.gridx = 0; gbc.gridy = labels.length; gbc.gridwidth = 2;
        panel.add(saveBtn, gbc);

        dialog.add(panel);

        saveBtn.addActionListener(e -> {
            String type = (String) typeBox.getSelectedItem();
            String number = numberField.getText().trim();
            String status = (String) statusBox.getSelectedItem();

            if (number.isEmpty() || capacityField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vehicle number and capacity are required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int capacity;
            try {
                capacity = Integer.parseInt(capacityField.getText().trim());
                if (capacity <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Capacity must be a positive integer.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Vehicle vehicle = new Vehicle(
                    existing == null ? 0 : existing.getId(),
                    type, number, capacity, status
            );

            boolean success = existing == null
                    ? vehicleController.addVehicle(vehicle)
                    : vehicleController.updateVehicle(vehicle);

            if (success) {
                JOptionPane.showMessageDialog(dialog, existing == null ? "Vehicle added!" : "Vehicle updated!");
                loadData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Operation failed. Vehicle number may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }
}
