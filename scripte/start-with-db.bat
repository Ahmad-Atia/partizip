@echo off
echo Starting Partizip Application with MySQL Database...

echo.
echo Cleaning up previous containers...
docker-compose down -v

echo.
echo Building and starting all services...
docker-compose up --build

echo.
echo Application started! Services available at:
echo - API Gateway: http://localhost:8080
echo - User Service: http://localhost:8083
echo - Community Service: http://localhost:8081  
echo - Event Service: http://localhost:8082
echo - MySQL Database: localhost:3306
echo.
echo Press Ctrl+C to stop all services