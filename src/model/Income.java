package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Income model class representing an income entry
 */
public class Income {
    private int incomeId;
    private int userId;
    private String category;
    private BigDecimal amount;
    private Date date;
    private String notes;
    private Timestamp createdAt;

    // Constructors
    public Income() {}

    public Income(int userId, String category, BigDecimal amount, Date date) {
        this.userId = userId;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    public Income(int userId, String category, BigDecimal amount, Date date, String notes) {
        this.userId = userId;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.notes = notes;
    }

    // Getters and Setters
    public int getIncomeId() {
        return incomeId;
    }

    public void setIncomeId(int incomeId) {
        this.incomeId = incomeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Income{" +
                "incomeId=" + incomeId +
                ", userId=" + userId +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", notes='" + notes + '\'' +
                '}';
    }
}
