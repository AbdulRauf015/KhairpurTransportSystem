package views;
import controllers.RouteController;
import models.TransportRoute;
import utils.UI;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RoutePanel extends JPanel {
    private RouteController rc;
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtFrom, txtTo, txtDist, txtFare, txtDur, txtSearch;
    private JComboBox<String> cboType, cboStatus;
    private int selId = -1;

    public RoutePanel() {
        rc = new RouteController();
        setOpaque(false);
        setLayout(new BorderLayout(0, 14));
        init();
        load("");
    }

    private void init() {
        // ── FORM CARD ──
        JPanel fc = UI.card();
        fc.setLayout(new BorderLayout(0, 14));

        JLabel title = new JLabel("Route Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(UI.DARK);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

        JPanel fields = new JPanel(new GridLayout(4, 4, 14, 10));
        fields.setOpaque(false);

        txtFrom = sf("e.g. Khairpur");
        txtTo   = sf("e.g. Sukkur");
        txtDist = sf("e.g. 80");
        txtFare = sf("e.g. 150");
        txtDur  = sf("e.g. 2 Hours");

        cboType = UI.combo();
        for (String t : new String[]{"Bus","Car","Bike","Airline"}) cboType.addItem(t);

        cboStatus = UI.combo();
        for (String s : new String[]{"Active","Inactive"}) cboStatus.addItem(s);

        // Row 1
        fields.add(lbl("Transport Type:")); fields.add(cboType);
        fields.add(lbl("From City:"));      fields.add(txtFrom);
        // Row 2
        fields.add(lbl("To City:"));        fields.add(txtTo);
        fields.add(lbl("Distance (km):"));  fields.add(txtDist);
        // Row 3
        fields.add(lbl("Fare (Rs):"));      fields.add(txtFare);
        fields.add(lbl("Duration:"));       fields.add(txtDur);
        // Row 4
        fields.add(lbl("Status:"));         fields.add(cboStatus);
        fields.add(new JLabel());           fields.add(new JLabel());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnRow.setOpaque(false);
        JButton bAdd = UI.wideBtn("+ Add",    UI.GREEN, 120);
        JButton bUpd = UI.wideBtn("✎ Update", UI.BLUE,  120);
        JButton bDel = UI.wideBtn("✕ Delete", UI.RED,   120);
        JButton bClr = UI.wideBtn("↺ Clear",  UI.GRAY,  120);
        bAdd.addActionListener(e -> addRoute());
        bUpd.addActionListener(e -> updateRoute());
        bDel.addActionListener(e -> deleteRoute());
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

        String[] cols = {"ID","Type","From","To","Distance","Fare","Duration","Status"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        UI.styleTable(table);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(70);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.getColumnModel().getColumn(5).setPreferredWidth(80);
        table.getColumnModel().getColumn(6).setPreferredWidth(90);
        table.getColumnModel().getColumn(7).setPreferredWidth(75);

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
                cboType.setSelectedItem(model.getValueAt(row,1).toString());
                txtFrom.setText(model.getValueAt(row,2).toString());
                txtTo.setText(model.getValueAt(row,3).toString());
                String dist = model.getValueAt(row,4).toString().replace(" km","");
                txtDist.setText(dist);
                txtFare.setText(model.getValueAt(row,5).toString());
                txtDur.setText(model.getValueAt(row,6).toString());
                cboStatus.setSelectedItem(model.getValueAt(row,7).toString());
            }
        });
    }

    private void addRoute() {
        if (!validateForm()) return;
        TransportRoute r = buildRoute(0);
        if (rc.add(r)) {
            JOptionPane.showMessageDialog(this,"Route added!","Success",JOptionPane.INFORMATION_MESSAGE);
            clearForm(); load("");
        } else {
            JOptionPane.showMessageDialog(this,"Failed to add route!","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRoute() {
        if (selId == -1) { JOptionPane.showMessageDialog(this,"Select a route first!"); return; }
        if (!validateForm()) return;
        if (rc.update(buildRoute(selId))) {
            JOptionPane.showMessageDialog(this,"Route updated!","Success",JOptionPane.INFORMATION_MESSAGE);
            clearForm(); load("");
        }
    }

    private void deleteRoute() {
        if (selId == -1) { JOptionPane.showMessageDialog(this,"Select a route first!"); return; }
        if (JOptionPane.showConfirmDialog(this,"Delete this route?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            if (rc.delete(selId)) { clearForm(); load(""); }
        }
    }

    private TransportRoute buildRoute(int id) {
        int dist = 0;
        try { dist = Integer.parseInt(txtDist.getText().trim()); } catch (Exception ignored) {}
        double fare = 0;
        try { fare = Double.parseDouble(txtFare.getText().trim()); } catch (Exception ignored) {}
        return new TransportRoute(id, cboType.getSelectedItem().toString(),
            txtFrom.getText().trim(), txtTo.getText().trim(),
            dist, fare, txtDur.getText().trim(),
            cboStatus.getSelectedItem().toString());
    }

    private boolean validateForm() {
        if (txtFrom.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"From city required!"); return false; }
        if (txtTo.getText().trim().isEmpty())   { JOptionPane.showMessageDialog(this,"To city required!"); return false; }
        if (txtFare.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"Fare required!"); return false; }
        try { Double.parseDouble(txtFare.getText().trim()); }
        catch (Exception e) { JOptionPane.showMessageDialog(this,"Fare must be a number!"); return false; }
        return true;
    }

    private void clearForm() {
        txtFrom.setText(""); txtTo.setText(""); txtDist.setText("");
        txtFare.setText(""); txtDur.setText("");
        cboType.setSelectedIndex(0); cboStatus.setSelectedIndex(0);
        selId = -1; table.clearSelection();
    }

    private void load(String kw) {
        model.setRowCount(0);
        List<TransportRoute> list = kw.isEmpty() ? rc.getAll() : rc.search(kw);
        for (TransportRoute r : list)
            model.addRow(new Object[]{r.getId(), r.getTransportType(),
                r.getFromCity(), r.getToCity(),
                r.getDistanceKm()+" km", r.getFare(),
                r.getDuration(), r.getStatus()});
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
