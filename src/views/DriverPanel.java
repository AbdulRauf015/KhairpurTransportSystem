package views;
import controllers.DriverController;
import models.Driver;
import utils.UI;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DriverPanel extends JPanel {
    private DriverController dc;
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtName, txtLic, txtContact, txtAddr, txtSearch;
    private JComboBox<String> cboType, cboStatus;
    private int selId = -1;

    public DriverPanel() {
        dc = new DriverController();
        setOpaque(false);
        setLayout(new BorderLayout(0, 14));
        init();
        load("");
    }

    private void init() {
        // ── FORM CARD ──
        JPanel fc = UI.card();
        fc.setLayout(new BorderLayout(0, 14));

        JLabel title = new JLabel("Driver Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(UI.DARK);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

        JPanel fields = new JPanel(new GridLayout(3, 4, 14, 10));
        fields.setOpaque(false);

        txtName    = sf("e.g. Muhammad Ali");
        txtLic     = sf("e.g. BUS-001");
        txtContact = sf("e.g. 03001234567");
        txtAddr    = sf("e.g. Khairpur");

        cboType = UI.combo();
        for (String t : new String[]{"Bus","Car","Bike","Airline"}) cboType.addItem(t);

        cboStatus = UI.combo();
        for (String s : new String[]{"Active","Inactive"}) cboStatus.addItem(s);

        // Row 1
        fields.add(lbl("Full Name:"));       fields.add(txtName);
        fields.add(lbl("License Number:"));  fields.add(txtLic);
        // Row 2
        fields.add(lbl("Contact Number:"));  fields.add(txtContact);
        fields.add(lbl("Transport Type:"));  fields.add(cboType);
        // Row 3
        fields.add(lbl("Address:"));         fields.add(txtAddr);
        fields.add(lbl("Status:"));          fields.add(cboStatus);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnRow.setOpaque(false);
        JButton bAdd = UI.wideBtn("+ Add",    UI.GREEN, 120);
        JButton bUpd = UI.wideBtn("✎ Update", UI.BLUE,  120);
        JButton bDel = UI.wideBtn("✕ Delete", UI.RED,   120);
        JButton bClr = UI.wideBtn("↺ Clear",  UI.GRAY,  120);
        bAdd.addActionListener(e -> addDriver());
        bUpd.addActionListener(e -> updateDriver());
        bDel.addActionListener(e -> deleteDriver());
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
        JButton bS = UI.btn("🔍 Search", UI.BLUE);
        JButton bR = UI.btn("↺ All",     UI.GRAY);
        bS.addActionListener(e -> load(txtSearch.getText().trim()));
        bR.addActionListener(e -> { txtSearch.setText(""); load(""); });
        searchRow.add(txtSearch); searchRow.add(bS); searchRow.add(bR);

        String[] cols = {"ID","Name","License","Contact","Type","Address","Status"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        UI.styleTable(table);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(4).setPreferredWidth(70);
        table.getColumnModel().getColumn(6).setPreferredWidth(80);

        JScrollPane sc = new JScrollPane(table);
        sc.setBorder(BorderFactory.createLineBorder(new Color(215,225,242)));
        sc.getViewport().setBackground(Color.WHITE);

        tc.add(searchRow, BorderLayout.NORTH);
        tc.add(sc,        BorderLayout.CENTER);

        add(fc, BorderLayout.NORTH);
        add(tc, BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                selId = Integer.parseInt(model.getValueAt(row,0).toString());
                txtName.setText(model.getValueAt(row,1).toString());
                txtLic.setText(model.getValueAt(row,2).toString());
                txtContact.setText(model.getValueAt(row,3).toString());
                cboType.setSelectedItem(model.getValueAt(row,4).toString());
                txtAddr.setText(model.getValueAt(row,5).toString());
                cboStatus.setSelectedItem(model.getValueAt(row,6).toString());
            }
        });
    }

    private void addDriver() {
        if (!validateForm()) return;
        Driver d = new Driver(0, txtName.getText().trim(), txtLic.getText().trim(),
            txtContact.getText().trim(), cboType.getSelectedItem().toString(),
            txtAddr.getText().trim(), cboStatus.getSelectedItem().toString());
        if (dc.add(d)) {
            JOptionPane.showMessageDialog(this,"Driver added!","Success",JOptionPane.INFORMATION_MESSAGE);
            clearForm(); load("");
        } else {
            JOptionPane.showMessageDialog(this,"Failed! License number may already exist.","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateDriver() {
        if (selId == -1) { JOptionPane.showMessageDialog(this,"Select a driver first!"); return; }
        if (!validateForm()) return;
        Driver d = new Driver(selId, txtName.getText().trim(), txtLic.getText().trim(),
            txtContact.getText().trim(), cboType.getSelectedItem().toString(),
            txtAddr.getText().trim(), cboStatus.getSelectedItem().toString());
        if (dc.update(d)) {
            JOptionPane.showMessageDialog(this,"Driver updated!","Success",JOptionPane.INFORMATION_MESSAGE);
            clearForm(); load("");
        }
    }

    private void deleteDriver() {
        if (selId == -1) { JOptionPane.showMessageDialog(this,"Select a driver first!"); return; }
        if (JOptionPane.showConfirmDialog(this,"Delete this driver?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            if (dc.delete(selId)) { clearForm(); load(""); }
        }
    }

    private boolean validateForm() {
        if (txtName.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"Name required!"); return false; }
        if (txtLic.getText().trim().isEmpty())  { JOptionPane.showMessageDialog(this,"License number required!"); return false; }
        if (txtContact.getText().trim().isEmpty()){ JOptionPane.showMessageDialog(this,"Contact required!"); return false; }
        return true;
    }

    private void clearForm() {
        txtName.setText(""); txtLic.setText(""); txtContact.setText(""); txtAddr.setText("");
        cboType.setSelectedIndex(0); cboStatus.setSelectedIndex(0);
        selId = -1; table.clearSelection();
    }

    private void load(String kw) {
        model.setRowCount(0);
        List<Driver> list = kw.isEmpty() ? dc.getAll() : dc.search(kw);
        for (Driver d : list)
            model.addRow(new Object[]{d.getId(),d.getName(),d.getLicenseNumber(),
                d.getContact(),d.getTransportType(),d.getAddress(),d.getStatus()});
    }

    private JTextField sf(String tip) {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI",Font.PLAIN,13));
        f.setToolTipText(tip);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200,210,230)),
            BorderFactory.createEmptyBorder(5,10,5,10)));
        return f;
    }
    private JLabel lbl(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI",Font.BOLD,12));
        l.setForeground(new Color(40,60,100));
        return l;
    }
}
