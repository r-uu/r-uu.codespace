@echo off
echo ========================================
echo   Embedded Jetty Server + Hibernate
echo ========================================
echo.

set PROJECT_ROOT=%~dp0..\..\..\..\..
set WS_RS_DIR=%PROJECT_ROOT%\r-uu\app\jeeeraaah\backend\api\ws.rs
m
echo 1. Stopping Liberty server...
cd /d "%WS_RS_DIR%"
call mvn liberty:stop >nul 2>&1

echo 2. Switching to embedded Jetty + Hibernate configuration...
copy "%WS_RS_DIR%\src\main\wildfly\config\persistence-wildfly-hibernate.xml" "%WS_RS_DIR%\src\main\resources\META-INF\persistence.xml" >nul

echo 3. Creating embedded server configuration...
mkdir "%WS_RS_DIR%\src\main\java\de\ruu\app\jeeeraaah\backend\embedded" 2>nul

echo 4. Building and starting embedded server...
call mvn clean compile exec:java -Dexec.mainClass="de.ruu.app.jeeeraaah.backend.embedded.EmbeddedServer" -Dexec.args="8080"

echo.
echo ✅ Embedded server started on http://localhost:8080
echo ✅ API available at: http://localhost:8080/taskgroup/allFlat
echo.
pause
