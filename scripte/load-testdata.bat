@echo off
echo Loading test data from 01-init.sql...
echo.

echo Checking if MySQL container is running...
docker ps | findstr partizip-mysql >nul
if %errorlevel% neq 0 (
    echo Error: MySQL container is not running
    echo Please start the application first with: docker-compose up
    pause
    exit /b 1
)

echo.
echo Checking if init script exists...
if not exist "init-scripts\01-init.sql" (
    echo Error: 01-init.sql not found in init-scripts folder
    echo Please ensure the file exists at: init-scripts\01-init.sql
    pause
    exit /b 1
)

echo.
echo 1. Copying SQL script to container...
docker cp init-scripts\01-init.sql partizip-mysql:/tmp/01-init.sql

if %errorlevel% neq 0 (
    echo Error: Failed to copy SQL script to container
    pause
    exit /b 1
)

echo.
echo 2. Executing 01-init.sql script...
docker exec partizip-mysql mysql -u root -proot123 -e "source /tmp/01-init.sql"

if %errorlevel% neq 0 (
    echo Error: Failed to execute SQL script
    pause
    exit /b 1
)

echo.
echo 3. Verifying test data...
docker exec partizip-mysql mysql -u partizip_user -ppartizip_pass -e "USE partizip_db; SELECT COUNT(*) as total_users FROM users; SELECT user_id, name, lastname, email FROM users LIMIT 5;"

echo.
echo 4. Checking interests and participation...
docker exec partizip-mysql mysql -u partizip_user -ppartizip_pass -e "USE partizip_db; SELECT COUNT(*) as total_interests FROM user_interests; SELECT COUNT(*) as total_participation FROM user_participation;"

echo.
echo 5. Sample data overview...
docker exec partizip-mysql mysql -u partizip_user -ppartizip_pass -e "USE partizip_db; SELECT u.name, u.lastname, GROUP_CONCAT(ui.interest) as interests FROM users u LEFT JOIN user_interests ui ON u.user_id = ui.user_id GROUP BY u.user_id;"

echo.
echo Test data loaded successfully from 01-init.sql!
echo.
echo Available API endpoints:
echo - GET http://localhost:8083/users
echo - GET http://localhost:8083/health
echo - GET http://localhost:8083/search?name=John
echo.
pause