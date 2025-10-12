@echo off
echo ========================================
echo   Direct Test with WSL PostgreSQL
echo ========================================
echo.

set PROJECT_ROOT=%~dp0..\..\..\..\..
set WS_RS_DIR=%PROJECT_ROOT%\r-uu\app\jeeeraaah\backend\api\ws.rs

echo 1. Testing PostgreSQL connection via WSL...
wsl docker exec postgres psql -U app_jeeeraaah_test -d app_jeeeraaah -c "SELECT 'PostgreSQL Ready' as status;" 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Creating database and user in existing PostgreSQL...
    wsl docker exec postgres psql -U postgres -c "CREATE DATABASE app_jeeeraaah;" 2>nul
    wsl docker exec postgres psql -U postgres -c "CREATE USER app_jeeeraaah_test WITH PASSWORD 'app_jeeeraaah_test';" 2>nul
    wsl docker exec postgres psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE app_jeeeraaah TO app_jeeeraaah_test;" 2>nul
)

echo 2. Switching back to working OpenLiberty + EclipseLink...
cd /d "%WS_RS_DIR%"
copy "%WS_RS_DIR%\src\main\payara\config\persistence-payara-eclipselink.xml" "%WS_RS_DIR%\src\main\resources\META-INF\persistence.xml" >nul

echo 3. Updating server.xml with correct datasource configuration...
powershell -Command "(Get-Content '%WS_RS_DIR%\src\main\liberty\config\server.xml') -replace 'javax.sql.ConnectionPoolDataSource', 'javax.sql.DataSource' | Set-Content '%WS_RS_DIR%\src\main\liberty\config\server.xml'"

echo 4. Stopping any running Liberty instances...
call mvn liberty:stop >nul 2>&1

echo 5. Clean rebuild and restart...
call mvn clean compile >nul
call mvn liberty:dev &

echo 6. Waiting for server startup...
timeout /t 30 /nobreak >nul

echo 7. Testing API...
curl -i "http://localhost:9080/jeee-raaah/taskgroup/allFlat"

echo.
echo ✅ If successful: Server running on http://localhost:9080/jeee-raaah/
echo ✅ API Test: http://localhost:9080/jeee-raaah/taskgroup/allFlat
echo ✅ Admin Center: http://localhost:9080/adminCenter/
echo.
pause
