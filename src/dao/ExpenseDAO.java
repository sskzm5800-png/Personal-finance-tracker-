package dao;

import db.DatabaseConnection;
import model.Expense;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Expense operations
 */
public class ExpenseDAO {
    private final DatabaseConnection dbConnection;

    public ExpenseDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    /**
     * Add new expense entry
     * @param expense Expense object to add
     * @return true if successful, false otherwise
     */
    public boolean addExpense(Expense expense) {
        String sql = "INSERT INTO expenses (user_id, category, amount, date, notes) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, expense.getUserId());
            pstmt.setString(2, expense.getCategory());
            pstmt.setBigDecimal(3, expense.getAmount());
            pstmt.setDate(4, expense.getDate());
            pstmt.setString(5, expense.getNotes());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding expense: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all expenses for a user
     * @param userId User ID
     * @return List of expenses
     */
    public List<Expense> getExpensesByUserId(int userId) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expenses WHERE user_id = ? ORDER BY date DESC";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Expense expense = new Expense();
                    expense.setExpenseId(rs.getInt("expense_id"));
                    expense.setUserId(rs.getInt("user_id"));
                    expense.setCategory(rs.getString("category"));
                    expense.setAmount(rs.getBigDecimal("amount"));
                    expense.setDate(rs.getDate("date"));
                    expense.setNotes(rs.getString("notes"));
                    expense.setCreatedAt(rs.getTimestamp("created_at"));
                    expenses.add(expense);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting expenses: " + e.getMessage());
        }
        
        return expenses;
    }

    /**
     * Get expenses by month and year
     * @param userId User ID
     * @param month Month (1-12)
     * @param year Year (e.g., 2025)
     * @return List of expenses for the specified month/year
     */
    public List<Expense> getExpensesByMonth(int userId, int month, int year) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expenses WHERE user_id = ? " +
                     "AND MONTH(date) = ? AND YEAR(date) = ? " +
                     "ORDER BY date DESC";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, month);
            pstmt.setInt(3, year);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Expense expense = new Expense();
                    expense.setExpenseId(rs.getInt("expense_id"));
                    expense.setUserId(rs.getInt("user_id"));
                    expense.setCategory(rs.getString("category"));
                    expense.setAmount(rs.getBigDecimal("amount"));
                    expense.setDate(rs.getDate("date"));
                    expense.setNotes(rs.getString("notes"));
                    expense.setCreatedAt(rs.getTimestamp("created_at"));
                    expenses.add(expense);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting expenses by month: " + e.getMessage());
        }
        
        return expenses;
    }

    /**
     * Update an expense entry
     * @param expense Expense object with updated values
     * @return true if successful, false otherwise
     */
    public boolean updateExpense(Expense expense) {
        String sql = "UPDATE expenses SET category = ?, amount = ?, date = ?, notes = ? WHERE expense_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, expense.getCategory());
            pstmt.setBigDecimal(2, expense.getAmount());
            pstmt.setDate(3, expense.getDate());
            pstmt.setString(4, expense.getNotes());
            pstmt.setInt(5, expense.getExpenseId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating expense: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete an expense entry
     * @param expenseId Expense ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteExpense(int expenseId) {
        String sql = "DELETE FROM expenses WHERE expense_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, expenseId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting expense: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get expense by ID
     * @param expenseId Expense ID
     * @return Expense object or null if not found
     */
    public Expense getExpenseById(int expenseId) {
        String sql = "SELECT * FROM expenses WHERE expense_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, expenseId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Expense expense = new Expense();
                    expense.setExpenseId(rs.getInt("expense_id"));
                    expense.setUserId(rs.getInt("user_id"));
                    expense.setCategory(rs.getString("category"));
                    expense.setAmount(rs.getBigDecimal("amount"));
                    expense.setDate(rs.getDate("date"));
                    expense.setNotes(rs.getString("notes"));
                    expense.setCreatedAt(rs.getTimestamp("created_at"));
                    return expense;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting expense by ID: " + e.getMessage());
        }
        
        return null;
    }
}
