@echo off
echo ========================================
echo   Quick Payara Docker Setup with WSL PostgreSQL
echo ========================================
echo.

set PROJECT_ROOT=%~dp0..\..\..\..\..
set WS_RS_DIR=%PROJECT_ROOT%\r-uu\app\jeeeraaah\backend\api\ws.rs

echo 1. Testing WSL PostgreSQL connection...
wsl docker exec postgres psql -U postgres -c "SELECT version();" || (
    echo PostgreSQL not accessible via WSL
    goto :error
)

echo 2. Ensuring database and user exist...
wsl docker exec postgres psql -U postgres -c "SELECT 1 FROM pg_database WHERE datname = 'app_jeeeraaah';" | findstr "1" >nul || wsl docker exec postgres psql -U postgres -c "CREATE DATABASE app_jeeeraaah;"
wsl docker exec postgres psql -U postgres -c "SELECT 1 FROM pg_roles WHERE rolname = 'app_jeeeraaah_test';" | findstr "1" >nul || wsl docker exec postgres psql -U postgres -c "CREATE USER app_jeeeraaah_test WITH PASSWORD 'app_jeeeraaah_test';"
wsl docker exec postgres psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE app_jeeeraaah TO app_jeeeraaah_test;" 2>nul

echo 3. Activating Payara configuration...
cd /d "%WS_RS_DIR%"
copy "%WS_RS_DIR%\src\main\payara\config\persistence-payara-eclipselink.xml" "%WS_RS_DIR%\src\main\resources\META-INF\persistence.xml" >nul

echo 4. Building WAR file for Payara...
call mvn clean package -DskipTests -q

echo 5. Stopping Liberty server...
call mvn liberty:stop >nul 2>&1

echo 6. Starting Payara in Docker with WSL network access...
docker stop payara-jeeeraaah 2>nul
docker rm payara-jeeeraaah 2>nul

docker run -d ^
  --name payara-jeeeraaah ^
  --network host ^
  -p 8080:8080 ^
  -p 4848:4848 ^
  -v "%WS_RS_DIR%\target\r-uu.codespace.app.jeeeraaah.backend.api.ws.rs.war:/opt/payara/deployments/jeee-raaah.war:ro" ^
  payara/server-full:6.2024.6

echo 7. Waiting for Payara startup...
timeout /t 15 /nobreak >nul

echo 8. Testing Payara API...
curl -i "http://localhost:8080/jeee-raaah/taskgroup/allFlat"

echo.
echo ✅ Payara Server: http://localhost:8080/jeee-raaah/
echo ✅ API Test: http://localhost:8080/jeee-raaah/taskgroup/allFlat
echo ✅ Admin Console: http://localhost:4848/
echo.
goto :end

:error
echo ❌ WSL PostgreSQL not accessible
echo Please ensure Docker Compose is running in WSL Ubuntu
pause
exit /b 1

:end
pause
