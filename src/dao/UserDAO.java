package dao;

import db.DatabaseConnection;
import model.User;
import util.PasswordHasher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for User operations
 */
public class UserDAO {
    private final DatabaseConnection dbConnection;

    public UserDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    /**
     * Register a new user
     * @param username Username
     * @param password Plain text password (will be hashed)
     * @return true if registration successful, false otherwise
     */
    public boolean registerUser(String username, String password) {
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, PasswordHasher.hashPassword(password));
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Authenticate user login
     * @param username Username
     * @param password Plain text password
     * @return User object if authentication successful, null otherwise
     */
    public User loginUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");
                    
                    // Verify password
                    if (PasswordHasher.verifyPassword(password, storedHash)) {
                        User user = new User();
                        user.setUserId(rs.getInt("user_id"));
                        user.setUsername(rs.getString("username"));
                        user.setPasswordHash(storedHash);
                        user.setCreatedAt(rs.getTimestamp("created_at"));
                        return user;
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Check if username already exists
     * @param username Username to check
     * @return true if username exists, false otherwise
     */
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking username: " + e.getMessage());
        }
        
        return false;
    }

    /**
     * Update user password
     * @param userId User ID
     * @param newPassword New plain text password
     * @return true if update successful, false otherwise
     */
    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password_hash = ? WHERE user_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, PasswordHasher.hashPassword(newPassword));
            pstmt.setInt(2, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get user by ID
     * @param userId User ID
     * @return User object or null if not found
     */
    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    return user;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting user: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Get all users
     * @return List of all users
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                users.add(user);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all users: " + e.getMessage());
        }
        
        return users;
    }
}
