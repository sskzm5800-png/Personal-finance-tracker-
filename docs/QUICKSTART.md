# 🚀 QUICK START GUIDE - Personal Finance Tracker

## Before You Begin - IMPORTANT STEPS ⚠️

### 1. Setup MySQL Database (5 minutes)

**Step 1:** Open MySQL Command Line Client or MySQL Workbench

**Step 2:** Run this command to create the database:
```bash
mysql -u root -p < finance_db.sql
```

OR copy-paste the contents of `finance_db.sql` into MySQL Workbench and execute.

**Step 3:** Verify database is created:
```sql
SHOW DATABASES;  -- You should see 'finance_tracker'
USE finance_tracker;
SHOW TABLES;     -- You should see: users, incomes, expenses
```

### 2. Configure Database Connection (1 minute)

**Edit `config.properties` file:**
```properties
db.url=jdbc:mysql://localhost:3306/finance_tracker?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.username=root
db.password=YOUR_MYSQL_PASSWORD_HERE  ← CHANGE THIS!
```

**⚠️ IMPORTANT:** Replace `YOUR_MYSQL_PASSWORD_HERE` with your actual MySQL password!

### 3. Run the Application (30 seconds)

**Simply double-click:** `run.bat`

That's it! The application will:
- ✓ Check Java installation
- ✓ Compile all source files
- ✓ Launch the application

## 🎯 First Time Login Options

### Option 1: Use Demo Account
- Username: `demo`
- Password: `demo123`
- Already has sample income/expense data

### Option 2: Use Admin Account
- Username: `admin`
- Password: `admin123`
- Clean account to start fresh

### Option 3: Create New Account
- Click **Register** button
- Choose username (min 3 chars)
- Choose password (min 6 chars)

### Option 4: Guest Mode
- Click **Continue as Guest**
- Data won't be saved to database
- Perfect for testing!

## 📝 Quick Tasks to Try

After logging in:

1. **Add an Income:**
   - Click "💰 Income" in sidebar
   - Select category (e.g., Salary)
   - Enter amount (e.g., 50000)
   - Click "Add Income"

2. **Add an Expense:**
   - Click "💸 Expenses" in sidebar
   - Select category (e.g., Food)
   - Enter amount (e.g., 5000)
   - Click "Add Expense"

3. **View Summary:**
   - Click "📈 Summary" in sidebar
   - See your financial overview
   - View the bar chart
   - Click "Export Summary" to save report

## 🔧 Troubleshooting

### Problem: "Database connection failed"
**Solution:** 
1. Make sure MySQL is running
2. Check your password in `config.properties`
3. Verify database exists: `SHOW DATABASES;`
4. Use Guest Mode as backup option

### Problem: "javac is not recognized"
**Solution:**
1. Install Java 25 LTS
2. Add to PATH: Control Panel → System → Environment Variables
3. Verify: Open new cmd and type `java -version`

### Problem: Compile errors
**Solution:**
1. Delete the `bin` folder
2. Run `run.bat` again
3. Check error messages for details

## 📚 Project Structure Overview

```
Personal-finance-tracker-/
│
├── lib/                    # MySQL driver (DO NOT DELETE)
├── src/                    # All Java source code
├── bin/                    # Compiled classes (auto-generated)
├── Reports/                # Exported summaries go here
├── config.properties       # Database settings ← EDIT THIS
├── finance_db.sql         # Database setup ← RUN THIS FIRST
├── run.bat                # Double-click to run ← START HERE
└── README.md              # Full documentation
```

## 💡 Tips

- **Save regularly** - Your data is auto-saved to MySQL
- **Guest Mode** - Great for testing without database
- **Export Reports** - Backup your financial data
- **Categories** - Choose meaningful categories for better tracking
- **Date Format** - Always use YYYY-MM-DD (e.g., 2025-10-23)

## 🎉 You're Ready!

Double-click `run.bat` and start tracking your finances!

Need more help? Check the full README.md file.

---
Made with ❤️ using Java 25 LTS | Swing | MySQL 8.0
