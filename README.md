# Personal Finance Tracker 💰

A comprehensive desktop application for tracking personal income and expenses, built with **Java 25 LTS**, **Swing UI**, and **MySQL 8.0** database.

![Java](https://img.shields.io/badge/Java-25%20LTS-orange)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![License](https://img.shields.io/badge/License-MIT-green)

## 🌟 Features

### ✅ Core Functionality
- **User Authentication** - Secure login and registration system with password hashing (SHA-256)
- **Guest Mode** - Try the application without database setup
- **Income Tracking** - Record and categorize income sources (Salary, Freelance, Business, etc.)
- **Expense Management** - Track spending across multiple categories (Food, Rent, Bills, etc.)
- **Financial Summary** - View total income, expenses, and savings at a glance
- **Visual Charts** - Beautiful bar charts comparing income vs expenses
- **Export Reports** - Generate text-based summary reports

### 🎨 Modern UI
- Dark theme with Nimbus Look & Feel
- Intuitive navigation with dashboard
- Clean and responsive design
- Color-coded financial data (Income: Green, Expenses: Red, Savings: Blue)

### 🔒 Security
- Password hashing using SHA-256
- SQL injection protection with PreparedStatements
- Secure database connection management

## 📋 Prerequisites

Before running the application, ensure you have:

1. **Java Development Kit (JDK) 25 LTS** or later
   - Download from: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://jdk.java.net/)
   - Add Java to your system PATH

2. **MySQL 8.0 Community Server**
   - Download from: [MySQL Downloads](https://dev.mysql.com/downloads/mysql/)
   - Install and note your root password

3. **MySQL Connector/J** (Already included in `/lib` folder)

## 🚀 Installation & Setup

### Step 1: Clone the Repository
```bash
git clone https://github.com/sskzm5800-png/Personal-finance-tracker-.git
cd Personal-finance-tracker-
```

### Step 2: Setup MySQL Database

1. Start MySQL Server
2. Open MySQL Command Line Client or MySQL Workbench
3. Run the database schema script:

```bash
mysql -u root -p < finance_db.sql
```

Or manually execute the SQL commands from `finance_db.sql`

The script will:
- Create `finance_tracker` database
- Create tables: `users`, `incomes`, `expenses`
- Insert sample demo data

### Step 3: Configure Database Connection

**First-time setup:** Copy the template file and configure it with your credentials:

```bash
# Windows
copy config.properties.template config.properties

# Linux/Mac
cp config.properties.template config.properties
```

Then edit `config.properties` with your MySQL credentials:

```properties
db.url=jdbc:mysql://localhost:3306/finance_tracker?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.username=root
db.password=YOUR_MYSQL_PASSWORD
```

**⚠️ Important:** 
- Replace `YOUR_MYSQL_PASSWORD` with your actual MySQL root password
- The `config.properties` file is in `.gitignore` and will NOT be committed to Git
- Only `config.properties.template` is tracked in version control

### Step 4: Run the Application

#### Option 1: Using run.bat (Windows)
Simply double-click `run.bat` or run from command prompt:
```bash
run.bat
```

#### Option 2: Manual Compilation & Execution
```bash
# Compile
javac -cp ".;lib/mysql-connector-j.jar" -d bin -sourcepath src src/Main.java src/db/*.java src/model/*.java src/dao/*.java src/service/*.java src/ui/*.java src/util/*.java

# Run
java -cp "bin;lib/mysql-connector-j.jar" Main
```

## 👤 Default User Accounts

The database comes with two demo accounts:

| Username | Password  | Description |
|----------|-----------|-------------|
| `admin`  | `admin123` | Administrator account |
| `demo`   | `demo123`  | Demo user with sample data |

You can also:
- Create a new account using the **Register** button
- Use **Guest Mode** to try the app without database

## 📁 Project Structure

```
Personal-finance-tracker-/
│
├── lib/                          # External libraries
│   └── mysql-connector-j.jar     # MySQL JDBC driver
│
├── src/                          # Source code
│   ├── Main.java                 # Application entry point
│   ├── db/
│   │   └── DatabaseConnection.java
│   ├── model/
│   │   ├── User.java
│   │   ├── Income.java
│   │   └── Expense.java
│   ├── dao/
│   │   ├── UserDAO.java
│   │   ├── IncomeDAO.java
│   │   └── ExpenseDAO.java
│   ├── service/
│   │   └── FinanceService.java
│   ├── ui/
│   │   ├── LoginFrame.java
│   │   ├── DashboardFrame.java
│   │   ├── IncomePanel.java
│   │   ├── ExpensePanel.java
│   │   └── SummaryPanel.java
│   └── util/
│       └── PasswordHasher.java
│
├── bin/                          # Compiled classes (generated)
├── Reports/                      # Exported summary reports
├── config.properties             # Database configuration
├── finance_db.sql               # Database schema & sample data
├── run.bat                      # Windows build & run script
└── README.md                    # This file
```

## 🎯 Usage Guide

### 1. Login / Registration
- **Login:** Enter username and password
- **Register:** Create a new account (username must be unique, password ≥6 chars)
- **Guest Mode:** Try the app without saving data to database

### 2. Dashboard Navigation
After login, use the left sidebar to navigate:
- **📊 Dashboard** - Welcome screen with quick overview
- **💰 Income** - Add and manage income entries
- **💸 Expenses** - Track your spending
- **📈 Summary** - View financial summary with charts

### 3. Adding Income
1. Select income category (Salary, Freelance, etc.)
2. Enter amount (must be > 0)
3. Set date (format: YYYY-MM-DD)
4. Add optional notes
5. Click **Add Income**

### 4. Adding Expenses
1. Select expense category (Food, Rent, etc.)
2. Enter amount
3. Set date
4. Add optional notes
5. Click **Add Expense**

### 5. Viewing Summary
- See total income, total expenses, and savings
- View visual bar chart comparing income vs expenses
- Click **Refresh Data** to update the summary
- Click **Export Summary** to save a text report to `/Reports` folder

### 6. Deleting Entries
- Select a row in the Income or Expense table
- Click **Delete Selected**
- Confirm the deletion

## 🔧 Troubleshooting

### Database Connection Failed
**Problem:** Application shows "Database connection failed"
**Solutions:**
- Ensure MySQL server is running
- Verify credentials in `config.properties`
- Check that `finance_tracker` database exists
- Run `finance_db.sql` to create the database
- Use Guest Mode as fallback

### Compilation Errors
**Problem:** Java compilation fails
**Solutions:**
- Verify Java 25 LTS is installed: `java -version`
- Check JAVA_HOME environment variable
- Ensure `mysql-connector-j.jar` is in `/lib` folder
- Clean and rebuild: delete `/bin` folder and run `run.bat`

### UI Not Displaying Correctly
**Problem:** Dark theme not applied
**Solutions:**
- Application will use default theme if Nimbus is unavailable
- UI functionality remains the same

## 🤝 Contributing

Contributions are welcome! To contribute:

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/YourFeature`
3. Commit changes: `git commit -m 'Add YourFeature'`
4. Push to branch: `git push origin feature/YourFeature`
5. Open a Pull Request

## � Documentation

Additional documentation is available in the `/docs` folder:

- **[QUICKSTART.md](docs/QUICKSTART.md)** - Quick 5-minute setup guide
- **[SETUP.md](docs/SETUP.md)** - Detailed setup instructions for developers
- **[Copilot-Master-Prompt.md](docs/Copilot-Master-Prompt.md)** - Original project specifications

## �📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**sskzm5800-png**
- GitHub: [@sskzm5800-png](https://github.com/sskzm5800-png)

## 🙏 Acknowledgments

- Java Swing for the GUI framework
- MySQL for the robust database system
- Nimbus Look & Feel for the modern UI theme

## 📞 Support

If you encounter any issues or have questions:
1. Check the [Troubleshooting](#-troubleshooting) section
2. Open an issue on [GitHub Issues](https://github.com/sskzm5800-png/Personal-finance-tracker-/issues)
3. Review the code documentation in source files

---

**Happy Finance Tracking! 💰📊**

Made with ❤️ using Java 25 LTS