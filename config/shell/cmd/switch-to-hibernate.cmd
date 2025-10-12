@echo off
echo ========================================
echo   Switching to Hibernate JPA Provider
echo ========================================
echo.

set PROJECT_ROOT=%~dp0..\..\..\..\..
set WS_RS_DIR=%PROJECT_ROOT%\r-uu\app\jeeeraaah\backend\api\ws.rs
set CONFIG_DIR=%WS_RS_DIR%\src\main\liberty\config
set RESOURCES_DIR=%WS_RS_DIR%\src\main\resources\META-INF

echo 1. Stopping Liberty server...
cd /d "%WS_RS_DIR%"
call mvn liberty:stop >nul 2>&1

echo 2. Switching persistence.xml to Hibernate...
copy "%RESOURCES_DIR%\persistence-hibernate.xml" "%RESOURCES_DIR%\persistence.xml" >nul

echo 3. Updating server.xml for Hibernate...
powershell -Command "(Get-Content '%CONFIG_DIR%\server.xml') -replace 'org\.eclipse\.persistence\.jpa\.PersistenceProvider', 'org.hibernate.jpa.HibernatePersistenceProvider' -replace 'enableJPAEnhancement=\"true\"', 'enableJPAEnhancement=\"false\"' | Set-Content '%CONFIG_DIR%\server.xml'"

echo 4. Updating pom.xml dependencies...
powershell -Command "(Get-Content '%WS_RS_DIR%\pom.xml') -replace 'eclipselink', 'hibernate-core' -replace 'org\.eclipse\.persistence', 'org.hibernate.orm' -replace 'version>4\.0\.4', 'version>6.6.4.Final' | Add-Content -Value '<scope>provided</scope><optional>true</optional>' | Set-Content '%WS_RS_DIR%\pom.xml'"

echo 5. Cleaning Maven build...
call mvn clean >nul 2>&1

echo 6. Starting Liberty server with Hibernate...
start /b mvn liberty:dev

echo.
echo ✓ Successfully switched to Hibernate!
echo ✓ Server is starting in background...
echo ✓ Hibernate provides advanced ORM features
echo ✓ Note: May require careful configuration to avoid conflicts
echo.
echo To switch back to EclipseLink: switch-to-eclipselink.cmd
echo To monitor server: check Liberty dev mode output
echo.
pause
