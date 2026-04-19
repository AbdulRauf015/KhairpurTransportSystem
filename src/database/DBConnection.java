package database;
import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/khairpur_transport?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "root1234";
    private static Connection conn = null;
    private static String lastError = "";

    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(URL, USER, PASS);
                lastError = "";
            }
        } catch (ClassNotFoundException e) {
            lastError = "JDBC Driver not found! Add mysql-connector.jar to lib folder.";
        } catch (SQLException e) {
            lastError = "DB Connection failed!\n• Start MySQL Server\n• Run transport.sql\n• Password: root1234\n\nError: " + e.getMessage();
        }
        return conn;
    }
    public static boolean testConnection() { return getConnection() != null; }
    public static String getLastError() { return lastError; }
    public static void close() {
        try { if (conn != null && !conn.isClosed()) { conn.close(); conn = null; } } catch (SQLException ignored) {}
    }
}
