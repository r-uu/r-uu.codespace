@echo off
echo ========================================
echo   Starting WildFly with Local Setup
echo ========================================
echo.

set PROJECT_ROOT=%~dp0..\..\..\..\..
set WS_RS_DIR=%PROJECT_ROOT%\r-uu\app\jeeeraaah\backend\api\ws.rs
set WILDFLY_HOME=C:\wildfly
set WILDFLY_CONFIG=%WS_RS_DIR%\src\main\wildfly\config

echo 1. Checking WildFly installation...
if not exist "%WILDFLY_HOME%\bin\standalone.bat" (
    echo ❌ WildFly not found at %WILDFLY_HOME%
    echo.
    echo Please install WildFly 30.x from:
    echo https://www.wildfly.org/downloads/
    echo.
    echo Alternative: Use embedded server approach
    pause
    exit /b 1
)

echo 2. Stopping any running WildFly instances...
taskkill /F /IM java.exe 2>nul

echo 3. Copying PostgreSQL driver...
copy "%WS_RS_DIR%\src\main\liberty\config\lib\global\postgresql-42.7.5.jar" "%WILDFLY_HOME%\standalone\deployments\" >nul

echo 4. Configuring PostgreSQL datasource...
echo module add --name=org.postgresql --resources=%WILDFLY_HOME%/standalone/deployments/postgresql-42.7.5.jar --dependencies=javax.api,javax.transaction.api > "%TEMP%\wildfly-setup.cli"
echo /subsystem=datasources/jdbc-driver=postgresql:add(driver-name=postgresql,driver-module-name=org.postgresql,driver-class-name=org.postgresql.Driver) >> "%TEMP%\wildfly-setup.cli"
echo /subsystem=datasources/data-source=PostgreSQLDS:add(jndi-name=java:jboss/datasources/PostgreSQLDS,driver-name=postgresql,connection-url=jdbc:postgresql://localhost:5432/app_jeeeraaah,user-name=app_jeeeraaah_test,password=app_jeeeraaah_test,validate-on-match=true,background-validation=false) >> "%TEMP%\wildfly-setup.cli"
echo /subsystem=datasources/data-source=PostgreSQLDS:enable() >> "%TEMP%\wildfly-setup.cli"

echo 5. Starting WildFly server...
start "WildFly Server" /D "%WILDFLY_HOME%\bin" standalone.bat

echo 6. Waiting for server startup...
timeout /t 20 /nobreak >nul

echo 7. Applying configuration...
"%WILDFLY_HOME%\bin\jboss-cli.bat" --connect --file="%TEMP%\wildfly-setup.cli"

echo 8. Deploying application...
copy "%WS_RS_DIR%\target\r-uu.codespace.app.jeeeraaah.backend.api.ws.rs.war" "%WILDFLY_HOME%\standalone\deployments\jeee-raaah.war" >nul

echo.
echo ✅ WildFly + Hibernate setup complete!
echo ✅ Server: http://localhost:8080/jeee-raaah/
echo ✅ API Test: http://localhost:8080/jeee-raaah/taskgroup/allFlat
echo ✅ Admin: http://localhost:9990/console
echo.
echo Testing API in 5 seconds...
timeout /t 5 /nobreak >nul
curl -s "http://localhost:8080/jeee-raaah/taskgroup/allFlat" || echo "API test failed - server may still be starting"
echo.
pause
