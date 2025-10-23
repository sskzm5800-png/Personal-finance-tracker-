-- Grant permissions to user 'asus' for finance_tracker database

GRANT ALL PRIVILEGES ON finance_tracker.* TO 'asus'@'localhost';
FLUSH PRIVILEGES;

-- Verify permissions
SHOW GRANTS FOR 'asus'@'localhost';

SELECT 'Permissions granted successfully!' AS Status;
