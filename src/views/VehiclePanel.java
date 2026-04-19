package views;
import controllers.VehicleController;
import models.Vehicle;
import utils.UI;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.*;
import java.util.List;

public class VehiclePanel extends JPanel {
    private VehicleController vc;
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtNum, txtModel, txtCap, txtSearch;
    private JComboBox<String> cboType, cboStatus;
    private int selId = -1;

    public VehiclePanel() {
        vc = new VehicleController();
        setOpaque(false);
        setLayout(new BorderLayout(0, 14));
        init();
        load("");
    }

    private void init() {
        // ── FORM CARD ──
        JPanel fc = UI.card();
        fc.setLayout(new BorderLayout(0, 14));

        JLabel title = new JLabel("Vehicle Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(UI.DARK);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

        // Use GridLayout so fields don't get cut
        JPanel fields = new JPanel(new GridLayout(3, 4, 14, 10));
        fields.setOpaque(false);

        txtNum   = sf("e.g. KHP-B001");
        txtModel = sf("e.g. Toyota Corolla");
        txtCap   = sf("e.g. 45");

        cboType = UI.combo();
        for (String t : new String[]{"Bus", "Car", "Bike", "Airline"}) cboType.addItem(t);

        cboStatus = UI.combo();
        for (String s : new String[]{"Active", "Inactive", "Maintenance"}) cboStatus.addItem(s);

        // Row 1
        fields.add(lbl("Transport Type:")); fields.add(cboType);
        fields.add(lbl("Vehicle Number:")); fields.add(txtNum);
        // Row 2
        fields.add(lbl("Model Name:"));    fields.add(txtModel);
        fields.add(lbl("Capacity:"));      fields.add(txtCap);
        // Row 3
        fields.add(lbl("Status:"));        fields.add(cboStatus);
        fields.add(new JLabel());          fields.add(new JLabel());

        // Buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnRow.setOpaque(false);
        JButton bAdd = UI.wideBtn("+ Add",     UI.GREEN,  120);
        JButton bUpd = UI.wideBtn("✎ Update",  UI.BLUE,   120);
        JButton bDel = UI.wideBtn("✕ Delete",  UI.RED,    120);
        JButton bClr = UI.wideBtn("↺ Clear",   UI.GRAY,   120);
        bAdd.addActionListener(e -> addVehicle());
        bUpd.addActionListener(e -> updateVehicle());
        bDel.addActionListener(e -> deleteVehicle());
        bClr.addActionListener(e -> clearForm());
        btnRow.add(bAdd); btnRow.add(bUpd); btnRow.add(bDel); btnRow.add(bClr);

        fc.add(title,  BorderLayout.NORTH);
        fc.add(fields, BorderLayout.CENTER);
        fc.add(btnRow, BorderLayout.SOUTH);

        // ── TABLE CARD ──
        JPanel tc = UI.card();
        tc.setLayout(new BorderLayout(0, 10));

        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchRow.setOpaque(false);
        searchRow.add(lbl("Search:"));
        txtSearch = UI.field();
        txtSearch.setPreferredSize(new Dimension(280, 36));
        JButton bSearch  = UI.btn("🔍 Search", UI.BLUE);
        JButton bRefresh = UI.btn("↺ All",     UI.GRAY);
        bSearch.addActionListener(e  -> load(txtSearch.getText().trim()));
        bRefresh.addActionListener(e -> { txtSearch.setText(""); load(""); });
        searchRow.add(txtSearch); searchRow.add(bSearch); searchRow.add(bRefresh);

        String[] cols = {"ID", "Type", "Number", "Model", "Capacity", "Status"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        UI.styleTable(table);
        table.getColumnModel().getColumn(0).setMaxWidth(55);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setMaxWidth(90);

        JScrollPane sc = new JScrollPane(table);
        sc.setBorder(BorderFactory.createLineBorder(new Color(215, 225, 242)));
        sc.getViewport().setBackground(Color.WHITE);

        tc.add(searchRow, BorderLayout.NORTH);
        tc.add(sc,        BorderLayout.CENTER);

        add(fc, BorderLayout.NORTH);
        add(tc, BorderLayout.CENTER);

        // Row selection
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                selId = Integer.parseInt(model.getValueAt(row, 0).toString());
                cboType.setSelectedItem(model.getValueAt(row, 1).toString());
                txtNum.setText(model.getValueAt(row, 2).toString());
                txtModel.setText(model.getValueAt(row, 3).toString());
                txtCap.setText(model.getValueAt(row, 4).toString());
                cboStatus.setSelectedItem(model.getValueAt(row, 5).toString());
            }
        });
    }

    private void addVehicle() {
        if (!validateForm()) return;
        Vehicle v = buildVehicle(0);
        if (vc.add(v)) {
            JOptionPane.showMessageDialog(this, "Vehicle added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm(); load("");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add! Vehicle number may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateVehicle() {
        if (selId == -1) { JOptionPane.showMessageDialog(this, "Please select a vehicle first!"); return; }
        if (!validateForm()) return;
        if (vc.update(buildVehicle(selId))) {
            JOptionPane.showMessageDialog(this, "Vehicle updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm(); load("");
        }
    }

    private void deleteVehicle() {
        if (selId == -1) { JOptionPane.showMessageDialog(this, "Please select a vehicle first!"); return; }
        if (JOptionPane.showConfirmDialog(this, "Delete this vehicle?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (vc.delete(selId)) { clearForm(); load(""); }
        }
    }

    private Vehicle buildVehicle(int id) {
        return new Vehicle(id,
            cboType.getSelectedItem().toString(),
            txtNum.getText().trim(),
            txtModel.getText().trim(),
            txtCap.getText().trim().isEmpty() ? 1 : Integer.parseInt(txtCap.getText().trim()),
            cboStatus.getSelectedItem().toString());
    }

    private boolean validateForm() {
        if (txtNum.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vehicle number is required!"); return false;
        }
        if (!txtCap.getText().trim().isEmpty()) {
            try { Integer.parseInt(txtCap.getText().trim()); }
            catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Capacity must be a number!"); return false; }
        }
        return true;
    }

    private void clearForm() {
        txtNum.setText(""); txtModel.setText(""); txtCap.setText("");
        cboType.setSelectedIndex(0); cboStatus.setSelectedIndex(0);
        selId = -1; table.clearSelection();
    }

    private void load(String kw) {
        model.setRowCount(0);
        List<Vehicle> list = kw.isEmpty() ? vc.getAll() : vc.search(kw);
        for (Vehicle v : list)
            model.addRow(new Object[]{v.getId(), v.getTransportType(), v.getVehicleNumber(), v.getModel(), v.getCapacity(), v.getStatus()});
    }

    private JTextField sf(String tip) {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setToolTipText(tip);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 230)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        return f;
    }

    private JLabel lbl(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(new Color(40, 60, 100));
        return l;
    }
}
