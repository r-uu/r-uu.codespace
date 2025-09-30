@ECHO OFF
SETLOCAL

SET WRAPPER_DIR=.mvn\wrapper
SET WRAPPER_JAR=%WRAPPER_DIR%\maven-wrapper.jar
SET WRAPPER_PROPERTIES=%WRAPPER_DIR%\maven-wrapper.properties
SET WRAPPER_URL=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar

IF NOT EXIST "%WRAPPER_DIR%" (
  MKDIR "%WRAPPER_DIR%" >NUL 2>&1
)

IF NOT EXIST "%WRAPPER_JAR%" (
  ECHO [mvnw] Downloading Maven Wrapper JAR from %WRAPPER_URL%

  REM 1) Versuch: curl (Windows 10+)
  where curl >NUL 2>&1
  IF %ERRORLEVEL%==0 (
    ECHO [mvnw] Using curl
    curl -fL -o "%WRAPPER_JAR%" "%WRAPPER_URL%"
  )

  IF NOT EXIST "%WRAPPER_JAR%" (
    REM 2) Versuch: certutil
    ECHO [mvnw] Using certutil
    certutil -urlcache -split -f "%WRAPPER_URL%" "%WRAPPER_JAR%" >NUL 2>&1
  )

  IF NOT EXIST "%WRAPPER_JAR%" (
    REM 3) Versuch: bitsadmin
    ECHO [mvnw] Using bitsadmin
    SET "WRAPPER_JAR_ABS=%CD%\%WRAPPER_JAR%"
    bitsadmin /transfer "mavenWrapper" /download /priority normal "%WRAPPER_URL%" "%WRAPPER_JAR_ABS%" >NUL 2>&1
  )

  IF NOT EXIST "%WRAPPER_JAR%" (
    ECHO [mvnw] Failed to download Maven Wrapper JAR.
    EXIT /B 1
  )
)

:DL_OK
IF NOT DEFINED MAVEN_PROJECTBASEDIR SET MAVEN_PROJECTBASEDIR=%CD%

REM Prefer JAVA from JAVA_HOME if available
SET "JAVA_EXE=java"
IF DEFINED JAVA_HOME (
  IF EXIST "%JAVA_HOME%\bin\java.exe" SET "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
)

"%JAVA_EXE%" -classpath "%WRAPPER_JAR%" -Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR% org.apache.maven.wrapper.MavenWrapperMain %*

ENDLOCAL
