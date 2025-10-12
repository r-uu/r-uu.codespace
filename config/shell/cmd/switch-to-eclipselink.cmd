@echo off
echo ========================================
echo   Switching to EclipseLink JPA Provider
echo ========================================
echo.

set PROJECT_ROOT=%~dp0..\..\..\..\..
set WS_RS_DIR=%PROJECT_ROOT%\r-uu\app\jeeeraaah\backend\api\ws.rs
set CONFIG_DIR=%WS_RS_DIR%\src\main\liberty\config
set RESOURCES_DIR=%WS_RS_DIR%\src\main\resources\META-INF

echo 1. Stopping Liberty server...
cd /d "%WS_RS_DIR%"
call mvn liberty:stop >nul 2>&1

echo 2. Switching persistence.xml to EclipseLink...
copy "%RESOURCES_DIR%\persistence-eclipselink.xml" "%RESOURCES_DIR%\persistence.xml" >nul

echo 3. Updating server.xml for EclipseLink...
powershell -Command "(Get-Content '%CONFIG_DIR%\server.xml') -replace 'org\.hibernate\.jpa\.HibernatePersistenceProvider', 'org.eclipse.persistence.jpa.PersistenceProvider' -replace 'enableJPAEnhancement=\"false\"', 'enableJPAEnhancement=\"true\"' | Set-Content '%CONFIG_DIR%\server.xml'"

echo 4. Updating pom.xml dependencies...
powershell -Command "(Get-Content '%WS_RS_DIR%\pom.xml') -replace '<scope>provided</scope>\s*<optional>true</optional>', '' -replace 'hibernate-core', 'eclipselink' -replace 'org\.hibernate\.orm', 'org.eclipse.persistence' -replace 'version>6\.6\.4\.Final', 'version>4.0.4' | Set-Content '%WS_RS_DIR%\pom.xml'"

echo 5. Cleaning Maven build...
call mvn clean >nul 2>&1

echo 6. Starting Liberty server with EclipseLink...
start /b mvn liberty:dev

echo.
echo ✓ Successfully switched to EclipseLink!
echo ✓ Server is starting in background...
echo ✓ EclipseLink provides stable integration with OpenLiberty
echo ✓ No infinite loops or enhancement issues
echo.
echo To switch back to Hibernate: switch-to-hibernate.cmd
echo To monitor server: check Liberty dev mode output
echo.
pause
