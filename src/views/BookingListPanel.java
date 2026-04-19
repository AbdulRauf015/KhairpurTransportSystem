package views;
import controllers.*;
import models.*;
import utils.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.geom.*;
import java.time.LocalDate;
import java.util.*;

public class BookingListPanel extends JPanel {
    private BookingController bc; private RouteController rc; private VehicleController vc; private DriverController dc;
    private User user; private boolean isAdmin; private String filterType;
    private JTable table; private DefaultTableModel model;
    private JTextField txtSearch, txtPass, txtContact, txtSeat, txtNote, txtDate;
    private JComboBox<String> cboRoute, cboVehicle, cboDriver;
    private ArrayList<Integer> ids = new ArrayList<>();
    private int selId = -1;
    private Color typeColor;

    public BookingListPanel(User user, boolean isAdmin, String filterType) {
        this.user = user; this.isAdmin = isAdmin; this.filterType = filterType;
        bc = new BookingController(); rc = new RouteController();
        vc = new VehicleController(); dc = new DriverController();
        typeColor = filterType.equals("Bus") ? UI.BLUE : filterType.equals("Car") ? UI.GREEN
                  : filterType.equals("Bike") ? UI.ORG : UI.PURP;
        setOpaque(false);
        setLayout(new BorderLayout(0, 14));
        init();
        load("");
    }

    private void init() {
        String ico = filterType.equals("Bus") ? "🚌" : filterType.equals("Car") ? "🚗"
                   : filterType.equals("Bike") ? "🏍️" : "✈️";

        // ── BOOKING FORM CARD ──────────────────────────────────────
        JPanel fc = UI.card();
        fc.setLayout(new BorderLayout(0, 10));
        // NO fixed preferred size — let layout determine height

        // Header
        JLabel ft = new JLabel("Book " + filterType);
        ft.setFont(new Font("Segoe UI", Font.BOLD, 16));
        ft.setForeground(UI.DARK);
        ft.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));

        // ── FORM using GridLayout rows so nothing gets cut off ──
        JPanel form = new JPanel(new GridLayout(5, 4, 12, 10));
        form.setOpaque(false);

        // Initialize all fields
        txtPass    = styledField("e.g. Ahmed Ali");
        txtContact = styledField("e.g. 0300-1234567");
        txtSeat    = styledField("e.g. A1");
        txtNote    = styledField("Optional");
        txtDate    = styledField(LocalDate.now().toString());
        txtDate.setText(LocalDate.now().toString());

        cboRoute   = UI.combo(); cboVehicle = UI.combo(); cboDriver = UI.combo();
        for (TransportRoute r : rc.getByType(filterType))
            cboRoute.addItem(r.getId() + " | " + r.getFromCity() + " → " + r.getToCity() + " (Rs." + String.format("%.0f", r.getFare()) + ")");
        for (Vehicle v : vc.getByType(filterType))
            cboVehicle.addItem(v.getId() + " | " + v.getVehicleNumber() + " - " + v.getModel());
        for (Driver d : dc.getByType(filterType))
            cboDriver.addItem(d.getId() + " | " + d.getName());

        // Row 1 — Passenger Name | Contact
        form.add(lbl("Passenger Name *")); form.add(txtPass);
        form.add(lbl("Contact Number"));   form.add(txtContact);
        // Row 2 — Route | Vehicle
        form.add(lbl("Select Route *"));
        form.add(cboRoute);
        form.add(lbl(filterType.equals("Airline") ? "Select Flight" : "Select Vehicle"));
        form.add(cboVehicle);
        // Row 3 — Driver | Travel Date
        form.add(lbl(filterType.equals("Airline") ? "Pilot" : "Driver"));
        form.add(cboDriver);
        form.add(lbl("Travel Date *"));
        form.add(txtDate);
        // Row 4 — Seat | Special Note
        form.add(lbl(filterType.equals("Airline") ? "Seat No (e.g.12A)" : filterType.equals("Bike") ? "Luggage Note" : "Seat No"));
        form.add(txtSeat);
        form.add(lbl("Special Note"));
        form.add(txtNote);
        // Row 5 — Buttons
        JButton bBook  = UI.wideBtn("Book " + filterType, typeColor, 190);
        JButton bClear = UI.btn("↺ Clear", UI.GRAY);
        bBook.addActionListener(e -> book());
        bClear.addActionListener(e -> clear());
        JPanel btnWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnWrap.setOpaque(false);
        btnWrap.add(bBook); btnWrap.add(bClear);
        form.add(btnWrap);
        // Fill remaining cells
        form.add(new JLabel()); form.add(new JLabel()); form.add(new JLabel());

        fc.add(ft,   BorderLayout.NORTH);
        fc.add(form, BorderLayout.CENTER);

        // ── BOOKING LIST CARD ──────────────────────────────────────
        JPanel tc = UI.card();
        tc.setLayout(new BorderLayout(0, 12));

        JLabel tl = new JLabel(filterType + " Booking History");
        tl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tl.setForeground(UI.DARK);

        JPanel tr = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        tr.setOpaque(false);
        txtSearch = UI.field();
        txtSearch.setPreferredSize(new Dimension(200, 34));

        JButton bs   = UI.btn("🔍 Search", UI.BLUE);
        JButton br2  = UI.btn("↺ All",    UI.GRAY);
        JButton bc2  = UI.btn("✕ Cancel", UI.RED);
        JButton bp   = UI.btn("🖨 Print",  UI.GREEN);

        bs.addActionListener(e  -> load(txtSearch.getText().trim()));
        br2.addActionListener(e -> { txtSearch.setText(""); load(""); });
        bc2.addActionListener(e -> cancel());
        bp.addActionListener(e  -> print());

        if (isAdmin) {
            JButton bd  = UI.btn("🗑 Delete",    new Color(160, 30, 30));
            JButton bpa = UI.btn("📋 Print All", UI.PURP);
            bd.addActionListener(e  -> delete());
            bpa.addActionListener(e -> printAll());
            tr.add(bd); tr.add(bpa);
        }
        tr.add(txtSearch); tr.add(bs); tr.add(br2);
        tr.add(Box.createHorizontalStrut(6));
        tr.add(bc2); tr.add(bp);

        JPanel tb = new JPanel(new BorderLayout());
        tb.setOpaque(false);
        tb.add(tl, BorderLayout.WEST);
        tb.add(tr, BorderLayout.EAST);

        String[] cols = {"#", "Booking No", "Type", "Passenger", "Route", "Vehicle", "Date", "Fare", "Status"};
        model = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        table = new JTable(model);
        UI.styleTable(table);
        table.getColumnModel().getColumn(0).setMaxWidth(40);

        // Status renderer
        table.getColumnModel().getColumn(8).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                JLabel l = new JLabel(); l.setFont(new Font("Segoe UI", Font.BOLD, 12));
                l.setHorizontalAlignment(SwingConstants.CENTER); l.setOpaque(true);
                String val = v != null ? v.toString() : "";
                if ("Booked".equals(val))    { l.setText("Booked");    l.setForeground(UI.GREEN); }
                else if ("Cancelled".equals(val)) { l.setText("Cancelled"); l.setForeground(UI.RED); }
                else { l.setText(val); l.setForeground(Color.GRAY); }
                if (s) l.setBackground(new Color(210, 228, 255));
                else   l.setBackground(r % 2 == 0 ? Color.WHITE : new Color(247, 250, 255));
                return l;
            }
        });

        JScrollPane sc = new JScrollPane(table);
        sc.setBorder(BorderFactory.createLineBorder(new Color(215, 225, 242)));
        sc.getViewport().setBackground(Color.WHITE);

        tc.add(tb, BorderLayout.NORTH);
        tc.add(sc, BorderLayout.CENTER);

        // Put form at top, table fills rest
        add(fc, BorderLayout.NORTH);
        add(tc, BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0 && row < ids.size()) selId = ids.get(row);
        });
    }

    // ── ACTIONS ─────────────────────────────────────────────────────
    private void book() {
        // Validate passenger name
        if (txtPass.getText().trim().isEmpty()) {
            txtPass.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UI.RED, 2),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)));
            JOptionPane.showMessageDialog(this, "⚠  Passenger Name is required!", "Validation", JOptionPane.WARNING_MESSAGE);
            txtPass.requestFocus();
            return;
        }
        resetBorder(txtPass);

        // Validate date
        if (txtDate.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠  Travel Date is required!", "Validation", JOptionPane.WARNING_MESSAGE);
            txtDate.requestFocus(); return;
        }
        if (cboRoute.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No routes available for " + filterType + "!", "Error", JOptionPane.ERROR_MESSAGE); return;
        }
        if (cboVehicle.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No vehicles available for " + filterType + "!", "Error", JOptionPane.ERROR_MESSAGE); return;
        }

        int rid = Integer.parseInt(cboRoute.getSelectedItem().toString().split("\\|")[0].trim());
        int vid = Integer.parseInt(cboVehicle.getSelectedItem().toString().split("\\|")[0].trim());
        int did = cboDriver.getItemCount() > 0 ? Integer.parseInt(cboDriver.getSelectedItem().toString().split("\\|")[0].trim()) : 1;

        TransportRoute route = rc.getById(rid);
        double fare = route != null ? route.getFare() : 0;

        Booking b = new Booking();
        b.setBookingNumber(bc.generateNumber());
        b.setTransportType(filterType);
        b.setPassengerName(txtPass.getText().trim());
        b.setPassengerContact(txtContact.getText().trim());
        b.setRouteId(rid); b.setVehicleId(vid); b.setDriverId(did);
        b.setTravelDate(txtDate.getText().trim());
        b.setFare(fare);
        b.setSeatNumber(txtSeat.getText().trim());
        b.setSpecialNote(txtNote.getText().trim());
        b.setStatus("Booked");
        b.setBookedBy(user.getId());

        if (bc.add(b)) {
            JOptionPane.showMessageDialog(this,
                "✅  Booking Confirmed!\n\nBooking No: " + b.getBookingNumber() +
                "\nPassenger: " + b.getPassengerName() +
                "\nFare: Rs. " + String.format("%.0f", fare),
                "Success", JOptionPane.INFORMATION_MESSAGE);
            clear(); load("");
        } else {
            JOptionPane.showMessageDialog(this, "Booking failed! Check database connection.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancel() {
        if (selId == -1) { JOptionPane.showMessageDialog(this, "Please select a booking first!"); return; }
        if (JOptionPane.showConfirmDialog(this, "Cancel this booking?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (bc.cancel(selId)) { selId = -1; load(""); }
        }
    }

    private void delete() {
        if (selId == -1) { JOptionPane.showMessageDialog(this, "Please select a booking first!"); return; }
        if (JOptionPane.showConfirmDialog(this, "Delete permanently?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (bc.delete(selId)) { selId = -1; load(""); }
        }
    }

    private void print() {
        if (selId == -1) { JOptionPane.showMessageDialog(this, "Please select a booking to print!"); return; }
        Booking b = bc.getById(selId);
        if (b != null) PrintUtil.printBooking(b);
    }

    private void printAll() {
        java.util.List<Booking> all = bc.getByType(filterType);
        if (all.isEmpty()) { JOptionPane.showMessageDialog(this, "No bookings to print!"); return; }
        PrintUtil.printList(all, filterType + " Bookings");
    }

    private void clear() {
        txtPass.setText(""); txtContact.setText(""); txtSeat.setText(""); txtNote.setText("");
        txtDate.setText(LocalDate.now().toString());
        resetBorder(txtPass);
        if (cboRoute.getItemCount() > 0) cboRoute.setSelectedIndex(0);
        if (cboVehicle.getItemCount() > 0) cboVehicle.setSelectedIndex(0);
    }

    private void load(String kw) {
        model.setRowCount(0); ids.clear();
        java.util.List<Booking> list;
        if (isAdmin) list = kw.isEmpty() ? bc.getByType(filterType) : filter(bc.search(kw), filterType);
        else         list = filterByType(bc.getByUser(user.getId()), filterType);
        int i = 1;
        for (Booking b : list) {
            model.addRow(new Object[]{i++, b.getBookingNumber(), b.getTransportType(),
                b.getPassengerName(), b.getRouteName(), b.getVehicleNumber(),
                b.getTravelDate(), "Rs." + String.format("%.0f", b.getFare()), b.getStatus()});
            ids.add(b.getId());
        }
    }

    private java.util.List<Booking> filter(java.util.List<Booking> l, String t) {
        java.util.List<Booking> r = new ArrayList<>();
        for (Booking b : l) if (t.equals(b.getTransportType())) r.add(b);
        return r;
    }
    private java.util.List<Booking> filterByType(java.util.List<Booking> l, String t) {
        java.util.List<Booking> r = new ArrayList<>();
        for (Booking b : l) if (t == null || t.isEmpty() || t.equals(b.getTransportType())) r.add(b);
        return r;
    }

    // ── HELPERS ─────────────────────────────────────────────────────
    private JTextField styledField(String placeholder) {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setToolTipText(placeholder);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 230)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        return f;
    }

    private JLabel lbl(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(new Color(40, 60, 100));
        return l;
    }

    private void resetBorder(JTextField f) {
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 230)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    }
}
