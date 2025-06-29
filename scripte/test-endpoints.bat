@echo off
echo Testing Partizip Microservices Endpoints...
echo.

REM Check if curl is available
curl --version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: curl is not available
    echo Please install curl or use a web browser to test endpoints
    pause
    exit /b 1
)

echo Waiting for services to start...
timeout /t 5 >nul

echo.
echo Testing individual services:
echo.

echo Testing Community Service (Port 8081):
curl -s http://localhost:8081/hello
echo.

echo Testing Event Service (Port 8082):
curl -s http://localhost:8082/hello
echo.

echo Testing User Service (Port 8083):
curl -s http://localhost:8083/hello
echo.

echo.
echo Testing through API Gateway (Port 8080):
echo.

echo Testing Community Service through Gateway:
curl -s http://localhost:8080/api/community/hello
echo.

echo Testing Event Service through Gateway:
curl -s http://localhost:8080/api/events/hello
echo.

echo Testing User Service through Gateway:
curl -s http://localhost:8080/api/users/hello
echo.

echo.
echo Testing health endpoints:
echo.

echo API Gateway Health:
curl -s http://localhost:8080/actuator/health
echo.

echo Community Service Health:
curl -s http://localhost:8081/actuator/health
echo.

echo Event Service Health:
curl -s http://localhost:8082/actuator/health
echo.

echo User Service Health:
curl -s http://localhost:8083/actuator/health
echo.

echo.
echo Test completed!
pause
