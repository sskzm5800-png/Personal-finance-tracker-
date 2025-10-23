# Database Connection Guide 🔌

## How the Database Connection Works

This guide explains how the Personal Finance Tracker establishes and manages database connections.

---

## 📋 Table of Contents
1. [Architecture Overview](#architecture-overview)
2. [Step-by-Step Connection Process](#step-by-step-connection-process)
3. [Key Components](#key-components)
4. [Testing the Connection](#testing-the-connection)
5. [Troubleshooting](#troubleshooting)

---

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                     Application Layer                        │
│  (Main.java, UI Components, DAO Classes)                    │
└───────────────────────────┬─────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│              DatabaseConnection.java (Singleton)             │
│  • Loads config.properties                                   │
│  • Manages JDBC connection                                   │
│  • Provides getConnection() method                           │
└───────────────────────────┬─────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│            MySQL Connector/J (JDBC Driver)                   │
│  • Located in lib/mysql-connector-j.jar                      │
│  • Translates Java calls to MySQL protocol                   │
└───────────────────────────┬─────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                  MySQL Server (Port 3306)                    │
│  • Database: finance_tracker                                 │
│  • Tables: users, incomes, expenses                          │
└─────────────────────────────────────────────────────────────┘
```

---

## 🔄 Step-by-Step Connection Process

### Step 1: Configuration File (`config.properties`)

```properties
db.url=jdbc:mysql://localhost:3306/finance_tracker?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.username=root
db.password=yourpassword
```

**Breakdown of the URL:**
- `jdbc:mysql://` - Protocol (JDBC for MySQL)
- `localhost` - Server address (your computer)
- `3306` - MySQL default port
- `finance_tracker` - Database name
- `useSSL=false` - Disable SSL for local development
- `allowPublicKeyRetrieval=true` - Allow password authentication
- `serverTimezone=UTC` - Set timezone to avoid warnings

### Step 2: DatabaseConnection Class (Singleton Pattern)

```java
private DatabaseConnection() {
    // 1. Load properties file
    Properties props = new Properties();
    FileInputStream fis = new FileInputStream("config.properties");
    props.load(fis);
    
    // 2. Extract connection details
    this.url = props.getProperty("db.url");
    this.username = props.getProperty("db.username");
    this.password = props.getProperty("db.password");
    
    // 3. Load MySQL JDBC driver
    Class.forName("com.mysql.cj.jdbc.Driver");
}
```

**Why Singleton?**
- Only ONE database connection instance throughout the app
- Prevents multiple unnecessary connections
- Improves performance and resource management

### Step 3: Getting a Connection

```java
public Connection getConnection() throws SQLException {
    // Check if connection exists and is open
    if (connection == null || connection.isClosed()) {
        // Create new connection using DriverManager
        connection = DriverManager.getConnection(url, username, password);
        System.out.println("✓ Database connection established");
    }
    return connection;
}
```

**What happens here:**
1. Checks if existing connection is valid
2. If not, creates new connection using:
   - Database URL
   - Username
   - Password
3. Returns the active connection object

### Step 4: Using the Connection in DAO Classes

Example from `UserDAO.java`:

```java
public User findByUsername(String username) {
    String sql = "SELECT * FROM users WHERE username = ?";
    
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            return new User(
                rs.getInt("user_id"),
                rs.getString("username"),
                rs.getString("password_hash")
            );
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
```

**Key Points:**
- `DatabaseConnection.getInstance()` - Get singleton instance
- `getConnection()` - Get active connection
- `PreparedStatement` - Prevents SQL injection
- Try-with-resources - Auto-closes connection

---

## 🔑 Key Components

### 1. **DatabaseConnection.java** (Singleton Manager)
- **Location:** `src/db/DatabaseConnection.java`
- **Purpose:** Centralized connection management
- **Methods:**
  - `getInstance()` - Get singleton instance
  - `getConnection()` - Get active connection
  - `testConnection()` - Verify connection works
  - `closeConnection()` - Close connection gracefully

### 2. **config.properties** (Configuration File)
- **Location:** Root directory (NOT committed to Git)
- **Purpose:** Store database credentials securely
- **Format:** Key-value pairs

### 3. **MySQL Connector/J** (JDBC Driver)
- **Location:** `lib/mysql-connector-j.jar`
- **Version:** Compatible with MySQL 8.0
- **Purpose:** Translate Java → MySQL protocol

### 4. **DAO Classes** (Data Access Objects)
- **Location:** `src/dao/`
- **Files:** UserDAO.java, IncomeDAO.java, ExpenseDAO.java
- **Purpose:** Execute SQL queries using the connection

---

## ✅ Testing the Connection

### Method 1: Using Main.java

The application automatically tests the connection on startup:

```java
public static void main(String[] args) {
    DatabaseConnection db = DatabaseConnection.getInstance();
    if (!db.testConnection()) {
        System.err.println("WARNING: Database connection failed!");
        System.err.println("You can still use Guest Mode.");
    }
    // Rest of the application...
}
```

### Method 2: Manual MySQL Test

```powershell
# Test connection from command line
mysql -u root -p -e "USE finance_tracker; SELECT COUNT(*) FROM users;"
```

### Method 3: Check Service Status

```powershell
# Check if MySQL service is running
Get-Service -Name MySQL80

# Expected output:
# Name     Status
# ----     ------
# MySQL80  Running
```

---

## 🔧 Troubleshooting

### Problem 1: "Database connection failed"

**Cause:** MySQL server not running or wrong credentials

**Solution:**
```powershell
# Start MySQL service
net start MySQL80

# Verify it's running
Get-Service -Name MySQL80
```

### Problem 2: "Access denied for user"

**Cause:** Wrong username or password in `config.properties`

**Solution:**
1. Open `config.properties`
2. Update credentials:
   ```properties
   db.username=root
   db.password=YOUR_ACTUAL_PASSWORD
   ```

### Problem 3: "Unknown database 'finance_tracker'"

**Cause:** Database not created

**Solution:**
```powershell
# Run the database setup script
Get-Content finance_db.sql | mysql -u root -p
```

### Problem 4: "No suitable driver found"

**Cause:** MySQL JDBC driver not in classpath

**Solution:**
1. Verify `lib/mysql-connector-j.jar` exists
2. Ensure `run.bat` includes it in classpath:
   ```batch
   java -cp "bin;lib/mysql-connector-j.jar" Main
   ```

---

## 🔐 Security Best Practices

### 1. Never Commit config.properties
```gitignore
# In .gitignore
config.properties
```

### 2. Use Template for Repository
```properties
# config.properties.template (safe to commit)
db.url=jdbc:mysql://localhost:3306/finance_tracker?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.username=root
db.password=YOUR_MYSQL_PASSWORD
```

### 3. Password Hashing
All user passwords are hashed using SHA-256 before storage:

```java
// In PasswordHasher.java
public static String hashPassword(String password) {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] hash = md.digest(password.getBytes());
    // Convert to hex string...
}
```

### 4. PreparedStatements (SQL Injection Prevention)
Always use `PreparedStatement` instead of concatenating SQL:

```java
// ✓ SAFE
String sql = "SELECT * FROM users WHERE username = ?";
PreparedStatement stmt = conn.prepareStatement(sql);
stmt.setString(1, username);

// ✗ UNSAFE (vulnerable to SQL injection)
String sql = "SELECT * FROM users WHERE username = '" + username + "'";
```

---

## 📊 Connection Flow Diagram

```
User Action (Login/Add Income/etc.)
        ↓
UI Component (LoginFrame/IncomePanel)
        ↓
Service Layer (FinanceService)
        ↓
DAO Layer (UserDAO/IncomeDAO)
        ↓
DatabaseConnection.getInstance().getConnection()
        ↓
Check if connection exists and is valid
        ↓
    ┌───────────────────┐
    │ Connection exists │──Yes──→ Return existing connection
    │   and is open?    │
    └───────────────────┘
           │
           No
           ↓
    Create new connection:
    DriverManager.getConnection(url, username, password)
           ↓
    Return new connection
           ↓
    Execute SQL query (SELECT/INSERT/UPDATE/DELETE)
           ↓
    Process ResultSet
           ↓
    Return data to UI
```

---

## 🎯 Quick Setup Checklist

- [✓] MySQL 8.0 installed
- [✓] MySQL service running (MySQL80)
- [✓] Database created (`finance_tracker`)
- [✓] Tables created (users, incomes, expenses)
- [✓] `config.properties` configured with correct credentials
- [✓] MySQL Connector/J jar in `lib/` folder
- [✓] Java 25 LTS installed
- [✓] Project compiled successfully

---

## 📝 Summary

**The connection process in 3 steps:**

1. **Load Configuration** → Read `config.properties` for URL, username, password
2. **Create Connection** → Use JDBC `DriverManager` to connect to MySQL
3. **Execute Queries** → DAO classes use the connection to run SQL queries

**Key Advantage:** Singleton pattern ensures the entire application shares ONE connection, improving performance and resource management.

---

## 🚀 Next Steps

1. Run the application: `.\run.bat`
2. Login with demo account:
   - Username: `demo`
   - Password: `demo123`
3. Explore the connection in action:
   - Add income → See `IncomeDAO` use connection
   - View summary → See `FinanceService` fetch data
   - Check database: `mysql -u root -p -e "USE finance_tracker; SELECT * FROM incomes;"`

---

**Need help?** Check the main [README.md](../README.md) or [SETUP.md](SETUP.md) for more information.

