@echo off
echo ========================================
echo   Switching to WildFly + Hibernate
echo ========================================
echo.

set PROJECT_ROOT=%~dp0..\..\..\..\..
set WS_RS_DIR=%PROJECT_ROOT%\r-uu\app\jeeeraaah\backend\api\ws.rs
set RESOURCES_DIR=%WS_RS_DIR%\src\main\resources\META-INF
set WILDFLY_CONFIG=%WS_RS_DIR%\src\main\wildfly\config

echo 1. Stopping Liberty server...
cd /d "%WS_RS_DIR%"
call mvn liberty:stop >nul 2>&1

echo 2. Installing WildFly persistence configuration...
copy "%WILDFLY_CONFIG%\persistence-wildfly-hibernate.xml" "%RESOURCES_DIR%\persistence.xml" >nul

echo 3. Rebuilding WAR file...
call mvn clean package -DskipTests >nul

echo 4. WildFly configuration ready!

echo.
echo ✓ Successfully configured for WildFly + Hibernate!
echo ✓ WildFly provides excellent Hibernate integration
echo ✓ Native Jakarta EE support
echo.
echo Next steps:
echo - Start WildFly server
echo - Deploy PostgreSQL driver module
echo - Configure datasource via CLI: %WILDFLY_CONFIG%\postgresql-datasource.cli
echo - Deploy the WAR file
echo.
pause
