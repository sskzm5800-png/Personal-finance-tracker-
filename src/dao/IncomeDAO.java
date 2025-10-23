package dao;

import db.DatabaseConnection;
import model.Income;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Income operations
 */
public class IncomeDAO {
    private final DatabaseConnection dbConnection;

    public IncomeDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    /**
     * Add new income entry
     * @param income Income object to add
     * @return true if successful, false otherwise
     */
    public boolean addIncome(Income income) {
        String sql = "INSERT INTO incomes (user_id, category, amount, date, notes) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, income.getUserId());
            pstmt.setString(2, income.getCategory());
            pstmt.setBigDecimal(3, income.getAmount());
            pstmt.setDate(4, income.getDate());
            pstmt.setString(5, income.getNotes());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding income: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all incomes for a user
     * @param userId User ID
     * @return List of incomes
     */
    public List<Income> getIncomesByUserId(int userId) {
        List<Income> incomes = new ArrayList<>();
        String sql = "SELECT * FROM incomes WHERE user_id = ? ORDER BY date DESC";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Income income = new Income();
                    income.setIncomeId(rs.getInt("income_id"));
                    income.setUserId(rs.getInt("user_id"));
                    income.setCategory(rs.getString("category"));
                    income.setAmount(rs.getBigDecimal("amount"));
                    income.setDate(rs.getDate("date"));
                    income.setNotes(rs.getString("notes"));
                    income.setCreatedAt(rs.getTimestamp("created_at"));
                    incomes.add(income);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting incomes: " + e.getMessage());
        }
        
        return incomes;
    }

    /**
     * Get incomes by month and year
     * @param userId User ID
     * @param month Month (1-12)
     * @param year Year (e.g., 2025)
     * @return List of incomes for the specified month/year
     */
    public List<Income> getIncomesByMonth(int userId, int month, int year) {
        List<Income> incomes = new ArrayList<>();
        String sql = "SELECT * FROM incomes WHERE user_id = ? " +
                     "AND MONTH(date) = ? AND YEAR(date) = ? " +
                     "ORDER BY date DESC";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, month);
            pstmt.setInt(3, year);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Income income = new Income();
                    income.setIncomeId(rs.getInt("income_id"));
                    income.setUserId(rs.getInt("user_id"));
                    income.setCategory(rs.getString("category"));
                    income.setAmount(rs.getBigDecimal("amount"));
                    income.setDate(rs.getDate("date"));
                    income.setNotes(rs.getString("notes"));
                    income.setCreatedAt(rs.getTimestamp("created_at"));
                    incomes.add(income);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting incomes by month: " + e.getMessage());
        }
        
        return incomes;
    }

    /**
     * Update an income entry
     * @param income Income object with updated values
     * @return true if successful, false otherwise
     */
    public boolean updateIncome(Income income) {
        String sql = "UPDATE incomes SET category = ?, amount = ?, date = ?, notes = ? WHERE income_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, income.getCategory());
            pstmt.setBigDecimal(2, income.getAmount());
            pstmt.setDate(3, income.getDate());
            pstmt.setString(4, income.getNotes());
            pstmt.setInt(5, income.getIncomeId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating income: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete an income entry
     * @param incomeId Income ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteIncome(int incomeId) {
        String sql = "DELETE FROM incomes WHERE income_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, incomeId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting income: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get income by ID
     * @param incomeId Income ID
     * @return Income object or null if not found
     */
    public Income getIncomeById(int incomeId) {
        String sql = "SELECT * FROM incomes WHERE income_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, incomeId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Income income = new Income();
                    income.setIncomeId(rs.getInt("income_id"));
                    income.setUserId(rs.getInt("user_id"));
                    income.setCategory(rs.getString("category"));
                    income.setAmount(rs.getBigDecimal("amount"));
                    income.setDate(rs.getDate("date"));
                    income.setNotes(rs.getString("notes"));
                    income.setCreatedAt(rs.getTimestamp("created_at"));
                    return income;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting income by ID: " + e.getMessage());
        }
        
        return null;
    }
}
