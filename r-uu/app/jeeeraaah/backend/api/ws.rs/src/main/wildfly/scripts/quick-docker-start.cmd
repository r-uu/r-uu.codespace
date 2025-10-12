@echo off
echo ========================================
echo   Quick Docker WildFly + Hibernate Setup
echo ========================================
echo.

set PROJECT_ROOT=%~dp0..\..\..\..\..
set WS_RS_DIR=%PROJECT_ROOT%\r-uu\app\jeeeraaah\backend\api\ws.rs

echo 1. Stopping any existing containers...
docker stop wildfly-jeeeraaah postgres-wildfly 2>nul
docker rm wildfly-jeeeraaah postgres-wildfly 2>nul

echo 2. Building WAR file with WildFly configuration...
cd /d "%WS_RS_DIR%"
call mvn clean package -DskipTests -q

echo 3. Starting PostgreSQL container...
docker run -d ^
  --name postgres-wildfly ^
  -e POSTGRES_DB=app_jeeeraaah ^
  -e POSTGRES_USER=app_jeeeraaah_test ^
  -e POSTGRES_PASSWORD=app_jeeeraaah_test ^
  -p 5432:5432 ^
  postgres:15

echo 4. Waiting for PostgreSQL to be ready...
timeout /t 10 /nobreak >nul

echo 5. Starting WildFly container with automatic deployment...
docker run -d ^
  --name wildfly-jeeeraaah ^
  --link postgres-wildfly:postgres ^
  -p 8080:8080 ^
  -p 9990:9990 ^
  -v "%WS_RS_DIR%\target\r-uu.codespace.app.jeeeraaah.backend.api.ws.rs.war:/opt/jboss/wildfly/standalone/deployments/jeee-raaah.war:ro" ^
  quay.io/wildfly/wildfly:30.0.1.Final-jdk17

echo 6. Waiting for WildFly to start...
timeout /t 15 /nobreak >nul

echo 7. Configuring PostgreSQL datasource...
docker exec wildfly-jeeeraaah /opt/jboss/wildfly/bin/jboss-cli.sh --connect --commands="/subsystem=datasources/jdbc-driver=postgresql:add(driver-name=postgresql,driver-module-name=org.postgresql),/subsystem=datasources/data-source=PostgreSQLDS:add(jndi-name=java:jboss/datasources/PostgreSQLDS,driver-name=postgresql,connection-url=jdbc:postgresql://postgres:5432/app_jeeeraaah,user-name=app_jeeeraaah_test,password=app_jeeeraaah_test),/subsystem=datasources/data-source=PostgreSQLDS:enable()"

echo.
echo ✅ WildFly + Hibernate + PostgreSQL running in Docker!
echo ✅ Application: http://localhost:8080/jeee-raaah/
echo ✅ API Test: http://localhost:8080/jeee-raaah/taskgroup/allFlat
echo ✅ Admin Console: http://localhost:9990/console
echo.

echo Testing API now...
timeout /t 3 /nobreak >nul
curl -i "http://localhost:8080/jeee-raaah/taskgroup/allFlat"

echo.
echo Container status:
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

pause
