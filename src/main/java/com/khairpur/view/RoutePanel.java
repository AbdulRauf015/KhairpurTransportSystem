package com.khairpur.view;

import com.khairpur.controller.RouteController;
import com.khairpur.model.TransportRoute;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Admin panel for managing transport routes (CRUD operations).
 */
public class RoutePanel extends JPanel {

    private final RouteController routeController = new RouteController();
    private JTable table;
    private DefaultTableModel tableModel;

    private static final String[] COLUMNS = {"ID", "Route Name", "From", "To", "Distance (km)", "Fare (Rs.)", "Type"};
    private static final String[] TRANSPORT_TYPES = {"Bus", "Car", "Bike", "Airline"};

    public RoutePanel() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title
        JLabel title = new JLabel("Transport Routes Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(13, 71, 161));
        add(title, BorderLayout.NORTH);

        // Table
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

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        JButton addBtn = createButton("Add Route", new Color(46, 125, 50));
        JButton editBtn = createButton("Edit Route", new Color(13, 71, 161));
        JButton deleteBtn = createButton("Delete Route", new Color(198, 40, 40));
        JButton refreshBtn = createButton("Refresh", new Color(69, 90, 100));

        addBtn.addActionListener(e -> showRouteDialog(null));
        editBtn.addActionListener(e -> editSelectedRoute());
        deleteBtn.addActionListener(e -> deleteSelectedRoute());
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
        btn.setPreferredSize(new Dimension(120, 32));
        return btn;
    }

    void loadData() {
        tableModel.setRowCount(0);
        List<TransportRoute> routes = routeController.getAllRoutes();
        for (TransportRoute r : routes) {
            tableModel.addRow(new Object[]{
                    r.getId(), r.getRouteName(), r.getFromLocation(),
                    r.getToLocation(), r.getDistance(), r.getFare(), r.getTransportType()
            });
        }
    }

    private TransportRoute getSelectedRoute() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a route first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);
        String from = (String) tableModel.getValueAt(row, 2);
        String to = (String) tableModel.getValueAt(row, 3);
        double dist = (double) tableModel.getValueAt(row, 4);
        double fare = (double) tableModel.getValueAt(row, 5);
        String type = (String) tableModel.getValueAt(row, 6);
        return new TransportRoute(id, name, from, to, dist, fare, type);
    }

    private void editSelectedRoute() {
        TransportRoute route = getSelectedRoute();
        if (route != null) showRouteDialog(route);
    }

    private void deleteSelectedRoute() {
        TransportRoute route = getSelectedRoute();
        if (route == null) return;
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete route '" + route.getRouteName() + "'?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (routeController.deleteRoute(route.getId())) {
                JOptionPane.showMessageDialog(this, "Route deleted successfully.");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete route.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showRouteDialog(TransportRoute existing) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                existing == null ? "Add New Route" : "Edit Route", true);
        dialog.setSize(420, 340);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 5, 6, 5);

        JTextField nameField = new JTextField(15);
        JTextField fromField = new JTextField(15);
        JTextField toField = new JTextField(15);
        JTextField distField = new JTextField(15);
        JTextField fareField = new JTextField(15);
        JComboBox<String> typeBox = new JComboBox<>(TRANSPORT_TYPES);

        if (existing != null) {
            nameField.setText(existing.getRouteName());
            fromField.setText(existing.getFromLocation());
            toField.setText(existing.getToLocation());
            distField.setText(String.valueOf(existing.getDistance()));
            fareField.setText(String.valueOf(existing.getFare()));
            typeBox.setSelectedItem(existing.getTransportType());
        }

        String[] labels = {"Route Name:", "From:", "To:", "Distance (km):", "Fare (Rs.):", "Transport Type:"};
        Component[] fields = {nameField, fromField, toField, distField, fareField, typeBox};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            panel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            panel.add(fields[i], gbc);
        }

        JButton saveBtn = new JButton(existing == null ? "Add Route" : "Save Changes");
        saveBtn.setBackground(new Color(13, 71, 161));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        gbc.gridx = 0; gbc.gridy = labels.length; gbc.gridwidth = 2;
        panel.add(saveBtn, gbc);

        dialog.add(panel);

        saveBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String from = fromField.getText().trim();
            String to = toField.getText().trim();
            String type = (String) typeBox.getSelectedItem();

            if (name.isEmpty() || from.isEmpty() || to.isEmpty() || fareField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Name, From, To, and Fare are required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double dist = 0, fare = 0;
            try {
                if (!distField.getText().isEmpty()) dist = Double.parseDouble(distField.getText().trim());
                fare = Double.parseDouble(fareField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Distance and Fare must be valid numbers.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            TransportRoute route = new TransportRoute(
                    existing == null ? 0 : existing.getId(),
                    name, from, to, dist, fare, type
            );

            boolean success;
            if (existing == null) {
                success = routeController.addRoute(route);
            } else {
                success = routeController.updateRoute(route);
            }

            if (success) {
                JOptionPane.showMessageDialog(dialog, existing == null ? "Route added!" : "Route updated!");
                loadData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Operation failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }
}
