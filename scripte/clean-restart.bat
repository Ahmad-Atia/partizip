@echo off
echo Cleaning up Docker containers and restarting Partizip...
echo.

echo 1. Stopping all containers...
docker-compose down --remove-orphans

echo.
echo 2. Removing any conflicting containers...
for /f "tokens=1" %%i in ('docker ps -aq --filter "name=partizip"') do docker rm -f %%i
for /f "tokens=1" %%i in ('docker ps -aq --filter "name=api-gateway"') do docker rm -f %%i
for /f "tokens=1" %%i in ('docker ps -aq --filter "name=user-service"') do docker rm -f %%i
for /f "tokens=1" %%i in ('docker ps -aq --filter "name=community-service"') do docker rm -f %%i
for /f "tokens=1" %%i in ('docker ps -aq --filter "name=event-service"') do docker rm -f %%i

echo.
echo 3. Cleaning up unused resources...
docker container prune -f
docker image prune -f

echo.
echo 4. Starting fresh containers...
docker-compose up --build

pause