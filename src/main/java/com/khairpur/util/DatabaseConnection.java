package com.khairpur.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utility class for managing the MySQL database connection.
 * Uses a singleton pattern to maintain a single connection throughout the application.
 */
public class DatabaseConnection {

    private static final String PROPERTIES_FILE = "/database.properties";
    private static String url;
    private static String username;
    private static String password;
    private static Connection connection;

    static {
        loadProperties();
    }

    private DatabaseConnection() {}

    private static void loadProperties() {
        Properties props = new Properties();
        try (InputStream is = DatabaseConnection.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (is != null) {
                props.load(is);
                url = props.getProperty("db.url",
                        "jdbc:mysql://localhost:3306/khairpur_transport?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
                username = props.getProperty("db.username", "root");
                password = props.getProperty("db.password", "root");
            } else {
                // Use defaults if properties file is not found
                url = "jdbc:mysql://localhost:3306/khairpur_transport?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
                username = "root";
                password = "root";
            }
        } catch (IOException e) {
            url = "jdbc:mysql://localhost:3306/khairpur_transport?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            username = "root";
            password = "root";
        }
    }

    /**
     * Returns the singleton database connection, creating it if necessary.
     *
     * @return active Connection instance
     * @throws SQLException if connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, username, password);
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver not found: " + e.getMessage());
        }
        return connection;
    }

    /**
     * Closes the database connection.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Tests whether the database connection can be established.
     *
     * @return true if connection is successful, false otherwise
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
