# Setup Instructions for Developers

## First Time Setup

### 1. Clone the Repository
```bash
git clone https://github.com/sskzm5800-png/Personal-finance-tracker-.git
cd Personal-finance-tracker-
```

### 2. Configure Database Connection

**IMPORTANT:** Do NOT modify `config.properties` directly in Git!

1. Copy the template file:
   ```bash
   # Windows
   copy config.properties.template config.properties
   
   # Linux/Mac
   cp config.properties.template config.properties
   ```

2. Edit `config.properties` with your actual MySQL credentials:
   ```properties
   db.username=your_mysql_username
   db.password=your_mysql_password
   ```

3. The `config.properties` file is in `.gitignore` and will NOT be committed to Git.

### 3. Setup MySQL Database

Run the database schema:
```bash
mysql -u root -p < finance_db.sql
```

### 4. Build and Run

```bash
# Windows
run.bat

# Or compile manually
compile.bat
```

## For Contributors

### Important Files

- **`config.properties.template`** - Template for database config (commit this)
- **`config.properties`** - Your local config with real password (DO NOT commit)
- **`.gitignore`** - Ensures config.properties stays local

### Before Committing

Always check that you're not committing sensitive data:
```bash
git status
git diff
```

Your `config.properties` should NOT appear in `git status` output.

### Directory Structure

```
Personal-finance-tracker-/
├── docs/                         # Documentation (you are here!)
│   ├── Copilot-Master-Prompt.md  # Original project prompt
│   ├── QUICKSTART.md             # Quick start guide
│   └── SETUP.md                  # This file
├── src/                          # Java source code
├── lib/                          # External libraries
├── config.properties.template    # DB config template (in Git)
├── config.properties             # Your local config (NOT in Git)
└── README.md                     # Main documentation
```

## Troubleshooting

### "config.properties not found"
- Run: `copy config.properties.template config.properties`
- Edit the new file with your MySQL password

### "config.properties appears in git status"
- This should NOT happen if `.gitignore` is correct
- Run: `git rm --cached config.properties`
- The file will remain on your disk but be removed from Git tracking

### Database Connection Failed
- Check MySQL is running
- Verify credentials in your local `config.properties`
- Test connection: `mysql -u root -p`
