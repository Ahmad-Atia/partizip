@echo off
echo Building Partizip Microservices...
echo.

REM Check if Maven is available
mvn --version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Maven is not installed or not in PATH
    echo Please install Maven and try again
    pause
    exit /b 1
)

echo Maven found, starting build...
echo.

REM Clean and compile all modules
mvn clean compile

if %errorlevel% equ 0 (
    echo.
    echo ✓ Build successful!
    echo.
    echo To run the application:
    echo   1. Using Docker: docker-compose up --build
    echo   2. Using Maven: mvn spring-boot:run (in each service directory)
    echo.
) else (
    echo.
    echo ✗ Build failed!
    echo Check the error messages above.
    echo.
)

pause
