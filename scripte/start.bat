@echo off
echo Starting Partizip Microservices with Docker Compose...
echo.

REM Check if Docker is available
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Docker is not installed or not running
    echo Please install Docker and ensure it's running
    pause
    exit /b 1
)

REM Check if Docker Compose is available
docker-compose --version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Docker Compose is not available
    echo Please install Docker Compose
    pause
    exit /b 1
)

echo Docker and Docker Compose found!
echo.
echo Building and starting all services...
echo This may take a few minutes on first run...
echo.

docker-compose up --build

echo.
echo Services stopped.
pause
