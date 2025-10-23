-- Personal Finance Tracker Database Schema
-- MySQL 8.0 Community Server

-- Drop database if exists and create fresh
DROP DATABASE IF EXISTS finance_tracker;
CREATE DATABASE finance_tracker;
USE finance_tracker;

-- Users table for authentication
CREATE TABLE users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Incomes table
CREATE TABLE incomes (
  income_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  category VARCHAR(50) NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  date DATE NOT NULL,
  notes VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Expenses table
CREATE TABLE expenses (
  expense_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  category VARCHAR(50) NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  date DATE NOT NULL,
  notes VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_incomes_user_date ON incomes(user_id, date);
CREATE INDEX idx_expenses_user_date ON expenses(user_id, date);

-- Insert sample data
-- Password: 'admin123' (hashed using SHA-256)
INSERT INTO users (username, password_hash) VALUES 
('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9'),
('demo', 'e6c3da5b206634d7f3f3586d747ffdb36b5c675757b380c6a5fe5c570c714349');

-- Sample incomes for demo user (user_id = 2)
INSERT INTO incomes (user_id, category, amount, date, notes) VALUES
(2, 'Salary', 50000.00, '2025-01-05', 'Monthly salary'),
(2, 'Freelance', 15000.00, '2025-01-15', 'Web design project'),
(2, 'Bonus', 10000.00, '2025-01-25', 'Performance bonus'),
(2, 'Investment', 5000.00, '2025-02-10', 'Stock dividends');

-- Sample expenses for demo user (user_id = 2)
INSERT INTO expenses (user_id, category, amount, date, notes) VALUES
(2, 'Rent', 15000.00, '2025-01-01', 'Monthly rent'),
(2, 'Food', 8000.00, '2025-01-10', 'Groceries and dining'),
(2, 'Transportation', 3000.00, '2025-01-12', 'Fuel and metro'),
(2, 'Utilities', 2500.00, '2025-01-15', 'Electricity and water'),
(2, 'Entertainment', 4000.00, '2025-01-20', 'Movies and subscriptions'),
(2, 'Healthcare', 5000.00, '2025-02-05', 'Medical checkup'),
(2, 'Shopping', 6000.00, '2025-02-12', 'Clothing and accessories');

-- Display summary
SELECT 'Database setup complete!' AS Status;
SELECT COUNT(*) AS 'Total Users' FROM users;
SELECT COUNT(*) AS 'Total Incomes' FROM incomes;
SELECT COUNT(*) AS 'Total Expenses' FROM expenses;
