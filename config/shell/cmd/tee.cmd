@echo off
setlocal

set "LOG=%~1"
if not defined LOG (
  echo Usage: %~nx0 logfile
  exit /b 1
)

rem Datei anlegen, falls nicht vorhanden
(type nul >> "%LOG%") 2>nul

rem JScript-Implementierung starten (echtes Streaming)
cscript //nologo "%~dp0tee.js" "%LOG%"

endlocal & exit /b %errorlevel%
