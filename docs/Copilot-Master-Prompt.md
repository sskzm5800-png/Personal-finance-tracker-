Copilot Master Prompt — Personal Finance Tracker (Java 25 + Swing + MySQL + JDBC, No Maven)
# MASTER PROMPT: Personal Finance Tracker (Java 25 LTS, Swing UI, MySQL 8.0, JDBC)

## GOAL
Develop a complete, beginner-friendly **Personal Finance Tracker** desktop application using:
- **Java 25 LTS**
- **Swing (AWT for layout)**
- **MySQL 8.0 Community Server**
- **JDBC (mysql-connector-j.jar)**
- **No Maven** — manual build via `run.bat`
- **IDE:** Visual Studio Code on Windows 11  

The app must track **Income**, **Expenses**, and generate a **Summary Report** (text + chart).  
It should run directly by double-clicking `run.bat`.

---

## FUNCTIONAL REQUIREMENTS
### 1️⃣ Front Page / Login
- Start screen with:
  - **Login** (for persistent users)
  - **Guest Mode** (temporary session)
- Login credentials stored in MySQL table `users`.
- Password reset option.

### 2️⃣ Dashboard Navigation
After login/guest entry, show three buttons:
- **Income**
- **Expense**
- **Summary**

### 3️⃣ Income Module
- Fields: *Type of Income* (Salary, Bonus …), *Amount*, *Date* (auto today).
- Button → “Add Income”.
- Stored in `incomes` table.
- Display table of all income records.

### 4️⃣ Expense Module
- Fields: *Type of Expense* (Food, Bills …), *Amount*, *Date*.
- Button → “Add Expense”.
- Stored in `expenses` table.
- Display table of expenses.

### 5️⃣ Summary Module
- Compute:
  - `Total Income`
  - `Total Expense`
  - `Savings = Income – Expense`
- Visualize with **bar/pie chart** using simple Swing `Graphics`.
- Filter by month/year.

### 6️⃣ Dark-Theme Modern UI
- Use Nimbus or FlatLaf dark look-and-feel.
- Rounded buttons, hover effects, clear panels.
- Centered JFrames; minimum res 800×600.

---

## DATABASE SCHEMA (MySQL 8.0)
```sql
CREATE DATABASE finance_tracker;
USE finance_tracker;

CREATE TABLE users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE incomes (
  income_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  category VARCHAR(50),
  amount DECIMAL(10,2),
  date DATE,
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE expenses (
  expense_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  category VARCHAR(50),
  amount DECIMAL(10,2),
  date DATE,
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

PROJECT STRUCTURE
PersonalFinanceTracker/
├── lib/
│   └── mysql-connector-j.jar
├── src/
│   ├── Main.java
│   ├── db/DatabaseConnection.java
│   ├── model/User.java
│   ├── model/Income.java
│   ├── model/Expense.java
│   ├── dao/UserDAO.java
│   ├── dao/IncomeDAO.java
│   ├── dao/ExpenseDAO.java
│   ├── service/FinanceService.java
│   ├── ui/LoginFrame.java
│   ├── ui/DashboardFrame.java
│   ├── ui/IncomePanel.java
│   ├── ui/ExpensePanel.java
│   ├── ui/SummaryPanel.java
│   └── util/PasswordHasher.java
├── config.properties
├── finance_db.sql
├── run.bat
└── README.md

CONFIGURATION

config.properties

db.url=jdbc:mysql://localhost:3306/finance_tracker
db.username=root
db.password=yourpassword

IMPLEMENTATION DETAILS
DatabaseConnection.java

Singleton returning one JDBC Connection.

Load credentials from config.properties.

Auto-test connection on startup.

DAO Classes

UserDAO: login + register + password reset.

IncomeDAO & ExpenseDAO: CRUD methods using PreparedStatement.

Use try-with-resources; close ResultSets properly.

FinanceService.java

Calculate totals and savings.

Provide monthly filter methods.

SummaryPanel.java

Fetch totals from FinanceService.

Draw chart (bar or pie) using Swing Graphics.

Display summary text below chart.

UI LAYOUT
LoginFrame

Username, Password fields.

Buttons: Login, Register, Guest Mode, Forgot Password.

Dark Nimbus Look & Feel.

DashboardFrame

Buttons: Income | Expense | Summary | Logout.

Center JFrame with GridLayout.

IncomePanel / ExpensePanel

Form fields + Add button.

JTable below listing entries.

Scrollable panel.

SummaryPanel

Totals shown at top.

Chart in center.

Export Summary to text file (Reports/summary_<month>.txt).

ERROR HANDLING / VALIDATION

Validate amount > 0.

Show error dialog if fields empty.

Catch SQLExceptions and display friendly JOptionPane messages.

Guest mode uses in-memory ArrayList (no DB save).

BUILD & RUN INSTRUCTIONS (Windows 11)
run.bat
@echo off
title Personal Finance Tracker
echo Compiling Java sources...
javac -cp ".;lib/mysql-connector-j.jar" -d bin src/**/*.java
if %errorlevel% neq 0 (
  echo Compilation failed.
  pause
  exit /b
)
echo Running Personal Finance Tracker...
java -cp "bin;lib/mysql-connector-j.jar" Main
pause


Usage

Install MySQL 8.0 and create DB using finance_db.sql.

Edit config.properties with your credentials.

Place mysql-connector-j.jar in /lib/.

Double-click run.bat.

OUTPUT EXAMPLE
Total Income: ₹25000
Total Expense: ₹18000
Savings: ₹7000


Chart → simple bar graph with colored bars for Income vs Expense.

DELIVERABLES TO GENERATE

All .java source files (filled in, no TODOs).

config.properties

finance_db.sql (schema + sample data)

run.bat

README.md with setup instructions and screenshots placeholders.

FINAL NOTE TO COPILOT

Generate a fully functional Java project with:

Login + Guest Mode

Income / Expense CRUD

Summary view + chart + export

Dark Swing UI

MySQL 8.0 backend via JDBC

One-click run using run.bat

All code must compile and run successfully on Windows 11 with Java 25 LTS.

project repo link: (https://github.com/sskzm5800-png/Personal-finance-tracker-)

mysql connector/j file already downloaded on the project root: D:\You know what PROJECTZZ\Personal-finance-tracker-\mysql-connector-j-9.4.0.jar
