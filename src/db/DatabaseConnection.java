package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton class for managing database connections
 * Loads configuration from config.properties file
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private String url;
    private String username;
    private String password;

    /**
     * Private constructor - loads database configuration
     */
    private DatabaseConnection() {
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("config.properties");
            props.load(fis);
            fis.close();

            this.url = props.getProperty("db.url");
            this.username = props.getProperty("db.username");
            this.password = props.getProperty("db.password");

            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading database configuration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get singleton instance of DatabaseConnection
     */
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    /**
     * Get active database connection
     * Creates new connection if none exists or connection is closed
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("✓ Database connection established successfully");
        }
        return connection;
    }

    /**
     * Test database connection
     * @return true if connection successful, false otherwise
     */
    public boolean testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ Database connection test: SUCCESS");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Database connection test: FAILED");
            System.err.println("Error: " + e.getMessage());
            System.err.println("\nPlease check:");
            System.err.println("1. MySQL server is running");
            System.err.println("2. Database 'finance_tracker' exists (run finance_db.sql)");
            System.err.println("3. Credentials in config.properties are correct");
        }
        return false;
    }

    /**
     * Close database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
