@echo off
REM Simple compile script for testing
echo Cleaning bin directory...
if exist "bin" rmdir /s /q bin
mkdir bin

echo.
echo Compiling Java files...
javac -cp ".;lib\mysql-connector-j.jar" -d bin -sourcepath src src\Main.java src\db\*.java src\model\*.java src\dao\*.java src\service\*.java src\ui\*.java src\util\*.java

if %errorlevel% equ 0 (
    echo.
    echo ✓ Compilation successful!
    echo You can now run: run.bat
) else (
    echo.
    echo ✗ Compilation failed!
    echo Check errors above.
)

pause
