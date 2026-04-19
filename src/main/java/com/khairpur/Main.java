package com.khairpur;

import com.khairpur.view.LoginForm;

import javax.swing.*;

/**
 * Entry point for the Khairpur Multi-Transport Management System.
 *
 * <p>Run this class to launch the application. Before running, ensure:
 * <ul>
 *   <li>MySQL is running on localhost:3306</li>
 *   <li>The database schema from sql/schema.sql has been executed</li>
 *   <li>Credentials in src/main/resources/database.properties are correct</li>
 * </ul>
 */
public class Main {

    public static void main(String[] args) {
        // Use system look and feel for better native appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Fall back to default look and feel
        }

        // Customise global UI defaults
        UIManager.put("Button.arc", 8);
        UIManager.put("Component.arc", 8);
        UIManager.put("TabbedPane.selectedBackground", new java.awt.Color(232, 240, 254));

        // Launch on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
        });
    }
}
