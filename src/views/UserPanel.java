package views;
import controllers.UserController;
import models.User;
import utils.UI;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserPanel extends JPanel {
    private UserController uc;
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtName, txtUser, txtPass, txtContact, txtSearch;
    private JComboBox<String> cboRole;
    private int selId = -1;

    public UserPanel() {
        uc = new UserController();
        setOpaque(false);
        setLayout(new BorderLayout(0, 14));
        init();
        load("");
    }

    private void init() {
        // ── FORM CARD ──
        JPanel fc = UI.card();
        fc.setLayout(new BorderLayout(0, 14));

        JLabel title = new JLabel("User Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(UI.DARK);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

        JPanel fields = new JPanel(new GridLayout(3, 4, 14, 10));
        fields.setOpaque(false);

        txtName    = sf("e.g. Ahmed Ali");
        txtUser    = sf("e.g. ahmed123");
        txtPass    = sf("e.g. pass123");
        txtContact = sf("e.g. 03001234567");

        cboRole = UI.combo();
        cboRole.addItem("user");
        cboRole.addItem("admin");

        // Row 1
        fields.add(lbl("Full Name:"));    fields.add(txtName);
        fields.add(lbl("Username:"));     fields.add(txtUser);
        // Row 2
        fields.add(lbl("Password:"));     fields.add(txtPass);
        fields.add(lbl("Contact:"));      fields.add(txtContact);
        // Row 3
        fields.add(lbl("Role:"));         fields.add(cboRole);
        fields.add(new JLabel());         fields.add(new JLabel());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnRow.setOpaque(false);
        JButton bAdd = UI.wideBtn("+ Add",    UI.GREEN, 120);
        JButton bUpd = UI.wideBtn("✎ Update", UI.BLUE,  120);
        JButton bDel = UI.wideBtn("✕ Delete", UI.RED,   120);
        JButton bClr = UI.wideBtn("↺ Clear",  UI.GRAY,  120);
        bAdd.addActionListener(e -> addUser());
        bUpd.addActionListener(e -> updateUser());
        bDel.addActionListener(e -> deleteUser());
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

        String[] cols = {"ID","Full Name","Username","Contact","Role"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        UI.styleTable(table);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);

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
                txtUser.setText(model.getValueAt(row,2).toString());
                txtContact.setText(model.getValueAt(row,3).toString());
                cboRole.setSelectedItem(model.getValueAt(row,4).toString());
            }
        });
    }

    private void addUser() {
        if (!validateForm(true)) return;
        User u = new User(0, txtUser.getText().trim(), txtPass.getText().trim(),
            cboRole.getSelectedItem().toString(), txtName.getText().trim());
        u.setContact(txtContact.getText().trim());
        if (uc.add(u)) {
            JOptionPane.showMessageDialog(this,"User added!","Success",JOptionPane.INFORMATION_MESSAGE);
            clearForm(); load("");
        } else {
            JOptionPane.showMessageDialog(this,"Failed! Username may already exist.","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUser() {
        if (selId == -1) { JOptionPane.showMessageDialog(this,"Select a user first!"); return; }
        if (!validateForm(false)) return;
        User u = new User(selId, txtUser.getText().trim(), txtPass.getText().trim(),
            cboRole.getSelectedItem().toString(), txtName.getText().trim());
        u.setContact(txtContact.getText().trim());
        if (uc.update(u)) {
            JOptionPane.showMessageDialog(this,"User updated!","Success",JOptionPane.INFORMATION_MESSAGE);
            clearForm(); load("");
        }
    }

    private void deleteUser() {
        if (selId == -1) { JOptionPane.showMessageDialog(this,"Select a user first!"); return; }
        if (JOptionPane.showConfirmDialog(this,"Delete this user?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            if (uc.delete(selId)) { clearForm(); load(""); }
        }
    }

    private boolean validateForm(boolean checkExists) {
        if (txtName.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"Full name required!"); return false; }
        if (txtUser.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"Username required!"); return false; }
        if (txtPass.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"Password required!"); return false; }
        if (checkExists && uc.exists(txtUser.getText().trim())) {
            JOptionPane.showMessageDialog(this,"Username already exists!"); return false;
        }
        return true;
    }

    private void clearForm() {
        txtName.setText(""); txtUser.setText(""); txtPass.setText(""); txtContact.setText("");
        cboRole.setSelectedIndex(0);
        selId = -1; table.clearSelection();
    }

    private void load(String kw) {
        model.setRowCount(0);
        List<User> list = kw.isEmpty() ? uc.getAll() : uc.search(kw);
        for (User u : list)
            model.addRow(new Object[]{u.getId(), u.getFullName(),
                u.getUsername(), u.getContact() != null ? u.getContact() : "-", u.getRole()});
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
