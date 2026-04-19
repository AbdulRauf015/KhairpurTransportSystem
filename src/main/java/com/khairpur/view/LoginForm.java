package com.khairpur.view;

import com.khairpur.controller.UserController;
import com.khairpur.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Login screen for the Khairpur Multi-Transport Management System.
 * Supports both Admin and User role authentication, plus new user registration.
 */
public class LoginForm extends JFrame {

    private final UserController userController = new UserController();

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginBtn;
    private JButton registerBtn;
    private JLabel statusLabel;

    public LoginForm() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Khairpur Multi-Transport Management System - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(480, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(13, 71, 161));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(13, 71, 161));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 10, 20));
        JLabel titleLabel = new JLabel("🚌 Khairpur Transport System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        JLabel subtitleLabel = new JLabel("Multi-Transport Management", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(200, 220, 255));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel loginTitle = new JLabel("Sign In to Your Account");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginTitle.setForeground(new Color(13, 71, 161));
        formPanel.add(loginTitle, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setForeground(Color.RED);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(statusLabel, gbc);

        gbc.gridy = 4; gbc.gridwidth = 1;
        loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(13, 71, 161));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        formPanel.add(loginBtn, gbc);

        gbc.gridx = 1;
        registerBtn = new JButton("Register");
        registerBtn.setBackground(new Color(46, 125, 50));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        registerBtn.setFocusPainted(false);
        registerBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        formPanel.add(registerBtn, gbc);

        // Footer hint
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(13, 71, 161));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 12, 10));
        JLabel hintLabel = new JLabel("Default Admin: admin / admin123", SwingConstants.CENTER);
        hintLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hintLabel.setForeground(new Color(180, 200, 255));
        footerPanel.add(hintLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Event listeners
        loginBtn.addActionListener(this::handleLogin);
        registerBtn.addActionListener(this::handleRegister);
        passwordField.addActionListener(this::handleLogin);
        usernameField.addActionListener(e -> passwordField.requestFocus());
    }

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter both username and password.");
            return;
        }

        User user = userController.login(username, password);
        if (user == null) {
            statusLabel.setText("Invalid username or password.");
            passwordField.setText("");
            return;
        }

        dispose();
        if (user.isAdmin()) {
            new AdminDashboard(user).setVisible(true);
        } else {
            new UserDashboard(user).setVisible(true);
        }
    }

    private void handleRegister(ActionEvent e) {
        new RegisterDialog(this, userController).setVisible(true);
    }

    // Inner dialog for user registration
    static class RegisterDialog extends JDialog {
        private final UserController userController;
        private JTextField nameField, usernameField, emailField, phoneField;
        private JPasswordField passwordField, confirmPasswordField;

        RegisterDialog(JFrame parent, UserController uc) {
            super(parent, "Register New Account", true);
            this.userController = uc;
            initComponents();
        }

        private void initComponents() {
            setSize(400, 380);
            setLocationRelativeTo(getOwner());
            setLayout(new BorderLayout());

            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);

            JLabel title = new JLabel("Create New Account");
            title.setFont(new Font("Segoe UI", Font.BOLD, 15));
            title.setForeground(new Color(46, 125, 50));
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            formPanel.add(title, gbc);

            String[] labels = {"Full Name:", "Username:", "Email:", "Phone:", "Password:", "Confirm Password:"};
            gbc.gridwidth = 1;
            nameField = new JTextField(15);
            usernameField = new JTextField(15);
            emailField = new JTextField(15);
            phoneField = new JTextField(15);
            passwordField = new JPasswordField(15);
            confirmPasswordField = new JPasswordField(15);
            JComponent[] fields = {nameField, usernameField, emailField, phoneField, passwordField, confirmPasswordField};

            for (int i = 0; i < labels.length; i++) {
                gbc.gridx = 0; gbc.gridy = i + 1;
                formPanel.add(new JLabel(labels[i]), gbc);
                gbc.gridx = 1;
                formPanel.add(fields[i], gbc);
            }

            JButton registerBtn = new JButton("Register");
            registerBtn.setBackground(new Color(46, 125, 50));
            registerBtn.setForeground(Color.WHITE);
            registerBtn.setFocusPainted(false);
            gbc.gridx = 0; gbc.gridy = labels.length + 1; gbc.gridwidth = 2;
            formPanel.add(registerBtn, gbc);

            add(formPanel, BorderLayout.CENTER);

            registerBtn.addActionListener(e -> doRegister());
        }

        private void doRegister() {
            String name = nameField.getText().trim();
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirm = new String(confirmPasswordField.getPassword());

            if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name, username, and password are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (userController.usernameExists(username)) {
                JOptionPane.showMessageDialog(this, "Username already taken. Please choose another.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            User user = new User(0, username, password, "user", name, email, phone);
            if (userController.register(user)) {
                JOptionPane.showMessageDialog(this, "Account created successfully! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
