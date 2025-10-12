@echo off
echo ========================================
echo   Complete Payara Setup with PostgreSQL
echo ========================================
echo.

set PROJECT_ROOT=%~dp0..\..\..\..\..
set WS_RS_DIR=%PROJECT_ROOT%\r-uu\app\jeeeraaah\backend\api\ws.rs

echo 1. Stopping all existing servers...
cd /d "%WS_RS_DIR%"
call mvn liberty:stop >nul 2>&1
docker stop payara-jeeeraaah postgres-wildfly 2>nul
docker rm payara-jeeeraaah postgres-wildfly 2>nul
taskkill /F /IM java.exe 2>nul

echo 2. Checking PostgreSQL in WSL...
echo Starting/checking PostgreSQL container in WSL...
wsl docker start postgres 2>nul || echo "PostgreSQL container not found, will create new one"

timeout /t 3 /nobreak >nul

echo Testing PostgreSQL connection...
wsl docker exec postgres psql -U postgres -c "SELECT version();" >nul 2>&1 || (
    echo Starting fresh PostgreSQL container...
    wsl docker run -d --name postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres:15
    timeout /t 10 /nobreak >nul
)

echo 3. Setting up database and user...
wsl docker exec postgres psql -U postgres -c "DROP DATABASE IF EXISTS app_jeeeraaah;" 2>nul
wsl docker exec postgres psql -U postgres -c "DROP USER IF EXISTS app_jeeeraaah_test;" 2>nul
wsl docker exec postgres psql -U postgres -c "CREATE DATABASE app_jeeeraaah;"
wsl docker exec postgres psql -U postgres -c "CREATE USER app_jeeeraaah_test WITH PASSWORD 'app_jeeeraaah_test';"
wsl docker exec postgres psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE app_jeeeraaah TO app_jeeeraaah_test;"
wsl docker exec postgres psql -U postgres -c "ALTER USER app_jeeeraaah_test CREATEDB;"

echo 4. Configuring for Payara + EclipseLink...
copy "%WS_RS_DIR%\src\main\payara\config\persistence-payara-eclipselink.xml" "%WS_RS_DIR%\src\main\resources\META-INF\persistence.xml" >nul

echo 5. Building WAR file completely fresh...
call mvn clean compile package -DskipTests -q

echo 6. Starting Payara Server in Docker...
docker run -d ^
  --name payara-jeeeraaah ^
  --network host ^
  -p 8080:8080 ^
  -p 4848:4848 ^
  -v "%WS_RS_DIR%\target\r-uu.codespace.app.jeeeraaah.backend.api.ws.rs.war:/opt/payara/deployments/jeee-raaah.war:ro" ^
  payara/server-full:6.2024.6

echo 7. Waiting for Payara startup (30 seconds)...
timeout /t 30 /nobreak >nul

echo 8. Testing Payara server status...
docker logs payara-jeeeraaah --tail 10

echo 9. Testing API endpoint...
echo.
echo Testing: http://localhost:8080/jeee-raaah/taskgroup/allFlat
curl -i "http://localhost:8080/jeee-raaah/taskgroup/allFlat" --connect-timeout 10

echo.
echo ========================================
echo   Payara Setup Complete
echo ========================================
echo.
echo ✅ PostgreSQL: Running in WSL Docker
echo ✅ Payara Server: http://localhost:8080/jeee-raaah/
echo ✅ API Endpoint: http://localhost:8080/jeee-raaah/taskgroup/allFlat
echo ✅ Admin Console: http://localhost:4848/
echo.
echo Container status:
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" | findstr payara

echo.
echo If API test failed, try again in 1-2 minutes for full startup.
pause
