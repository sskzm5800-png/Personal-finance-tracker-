@echo off
title Personal Finance Tracker - Build and Run
color 0A

echo.
echo ╔═══════════════════════════════════════════════════════════╗
echo ║                                                           ║
echo ║       PERSONAL FINANCE TRACKER - BUILD SCRIPT             ║
echo ║       Java 25 LTS ^| Swing UI ^| MySQL 8.0                  ║
echo ║                                                           ║
echo ╚═══════════════════════════════════════════════════════════╝
echo.

REM Check if Java is installed
echo [1/4] Checking Java installation...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ✗ ERROR: Java is not installed or not in PATH
    echo Please install Java 25 LTS and add it to your system PATH
    pause
    exit /b 1
)
echo ✓ Java found

REM Create bin directory if it doesn't exist
if not exist "bin" mkdir bin
echo ✓ Binary directory ready

echo.
echo [2/4] Compiling Java source files...
echo Compiling with packages: db, model, dao, service, ui, util
echo.

REM Compile all Java files with package structure
javac -cp ".;lib\mysql-connector-j.jar" -d bin -sourcepath src src\Main.java src\db\*.java src\model\*.java src\dao\*.java src\service\*.java src\ui\*.java src\util\*.java

if %errorlevel% neq 0 (
    echo.
    echo ✗ COMPILATION FAILED!
    echo Check the error messages above and fix any compilation errors.
    echo.
    pause
    exit /b 1
)

echo.
echo ✓ Compilation successful!
echo.

echo [3/4] Checking database configuration...
if not exist "config.properties" (
    echo ⚠️  WARNING: config.properties not found!
    echo Please create config.properties with your MySQL credentials.
    echo You can still run in Guest Mode.
) else (
    echo ✓ Configuration file found
)

if not exist "Reports" mkdir Reports
echo ✓ Reports directory ready

echo.
echo [4/4] Launching Personal Finance Tracker...
echo.
echo ═══════════════════════════════════════════════════════════
echo.

REM Run the application
java -cp "bin;lib\mysql-connector-j.jar" Main

echo.
echo ═══════════════════════════════════════════════════════════
echo Application closed.
echo.
pause
