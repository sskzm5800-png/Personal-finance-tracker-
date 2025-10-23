-- Add more sample financial data for testing
-- This adds data for user 'Arjun' (user_id = 3) and demo user

USE finance_tracker;

-- Sample incomes for Arjun (user_id = 3)
INSERT INTO incomes (user_id, category, amount, date, notes) VALUES
(3, 'Salary', 75000.00, '2025-10-01', 'October salary'),
(3, 'Freelance', 25000.00, '2025-10-05', 'Web development project'),
(3, 'Bonus', 15000.00, '2025-10-10', 'Performance bonus'),
(3, 'Investment', 8000.00, '2025-10-15', 'Dividend income'),
(3, 'Salary', 75000.00, '2025-09-01', 'September salary'),
(3, 'Freelance', 20000.00, '2025-09-12', 'Mobile app design'),
(3, 'Gift', 5000.00, '2025-09-20', 'Birthday gift'),
(3, 'Business', 30000.00, '2025-08-15', 'Consulting work');

-- Sample expenses for Arjun (user_id = 3)
INSERT INTO expenses (user_id, category, amount, date, notes) VALUES
(3, 'Rent', 20000.00, '2025-10-01', 'Monthly rent'),
(3, 'Food', 12000.00, '2025-10-05', 'Groceries and dining out'),
(3, 'Transportation', 5000.00, '2025-10-08', 'Fuel and public transport'),
(3, 'Utilities', 3500.00, '2025-10-10', 'Electricity, water, internet'),
(3, 'Entertainment', 8000.00, '2025-10-12', 'Movies, Netflix, games'),
(3, 'Healthcare', 4000.00, '2025-10-14', 'Doctor visit and medicines'),
(3, 'Shopping', 15000.00, '2025-10-16', 'Clothing and electronics'),
(3, 'Education', 10000.00, '2025-10-18', 'Online course subscription'),
(3, 'Rent', 20000.00, '2025-09-01', 'September rent'),
(3, 'Food', 10000.00, '2025-09-10', 'Groceries'),
(3, 'Bills', 4500.00, '2025-09-15', 'Phone and internet bills'),
(3, 'Transportation', 4000.00, '2025-09-20', 'Car maintenance'),
(3, 'Entertainment', 6000.00, '2025-09-22', 'Concert tickets'),
(3, 'Food', 8000.00, '2025-08-10', 'August groceries'),
(3, 'Rent', 20000.00, '2025-08-01', 'August rent');

-- Add more data for demo user (user_id = 2) if needed
INSERT INTO incomes (user_id, category, amount, date, notes) VALUES
(2, 'Salary', 50000.00, '2025-10-05', 'October salary'),
(2, 'Freelance', 12000.00, '2025-10-12', 'Side project'),
(2, 'Investment', 3000.00, '2025-10-20', 'Stock returns');

INSERT INTO expenses (user_id, category, amount, date, notes) VALUES
(2, 'Rent', 15000.00, '2025-10-01', 'October rent'),
(2, 'Food', 6000.00, '2025-10-08', 'Groceries'),
(2, 'Transportation', 2500.00, '2025-10-15', 'Metro and fuel'),
(2, 'Shopping', 4000.00, '2025-10-18', 'New shoes');

-- Display summary
SELECT 'Sample data added successfully!' AS Status;
SELECT user_id, username, COUNT(*) AS income_count FROM incomes 
JOIN users USING(user_id) 
GROUP BY user_id, username;
SELECT user_id, username, COUNT(*) AS expense_count FROM expenses 
JOIN users USING(user_id) 
GROUP BY user_id, username;

-- Show totals
SELECT 
    u.username,
    COALESCE(SUM(i.amount), 0) AS total_income,
    COALESCE(SUM(e.amount), 0) AS total_expenses,
    COALESCE(SUM(i.amount), 0) - COALESCE(SUM(e.amount), 0) AS savings
FROM users u
LEFT JOIN incomes i ON u.user_id = i.user_id
LEFT JOIN expenses e ON u.user_id = e.user_id
GROUP BY u.user_id, u.username
ORDER BY u.user_id;
