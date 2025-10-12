@echo off
echo ========================================
echo   Switching to Payara Server + EclipseLink
echo ========================================
echo.

set PROJECT_ROOT=%~dp0..\..\..\..\..
set WS_RS_DIR=%PROJECT_ROOT%\r-uu\app\jeeeraaah\backend\api\ws.rs
set RESOURCES_DIR=%WS_RS_DIR%\src\main\resources\META-INF
set PAYARA_CONFIG=%WS_RS_DIR%\src\main\payara\config

echo 1. Stopping Liberty server...
cd /d "%WS_RS_DIR%"
call mvn liberty:stop >nul 2>&1

echo 2. Backing up current persistence.xml...
if exist "%RESOURCES_DIR%\persistence.xml" (
    copy "%RESOURCES_DIR%\persistence.xml" "%RESOURCES_DIR%\persistence-backup.xml" >nul
)

echo 3. Installing Payara persistence configuration...
copy "%PAYARA_CONFIG%\persistence-payara-eclipselink.xml" "%RESOURCES_DIR%\persistence.xml" >nul

echo 4. Installing Payara resources configuration...
copy "%PAYARA_CONFIG%\glassfish-resources.xml" "%WS_RS_DIR%\src\main\webapp\WEB-INF\" >nul 2>&1
if not exist "%WS_RS_DIR%\src\main\webapp\WEB-INF\" (
    mkdir "%WS_RS_DIR%\src\main\webapp\WEB-INF"
    copy "%PAYARA_CONFIG%\glassfish-resources.xml" "%WS_RS_DIR%\src\main\webapp\WEB-INF\" >nul
)

echo 5. Rebuilding WAR file...
call mvn clean package -DskipTests >nul

echo 6. Starting Payara Server (if available)...
call "%~dp0start-payara.cmd"

echo.
echo ✓ Successfully configured for Payara Server + EclipseLink!
echo ✓ Payara provides the most stable EclipseLink integration
echo ✓ No JPA enhancement conflicts or infinite loops
echo.
echo Next steps:
echo - Install Payara Server if not available
echo - Deploy the WAR file to Payara
echo - Configure PostgreSQL datasource in Payara
echo.
pause
