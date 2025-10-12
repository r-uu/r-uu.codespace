@echo off
echo Switching to EclipseLink JPA Provider...
copy "%~dp0persistence-eclipselink.xml" "%~dp0persistence.xml" >nul
echo EclipseLink is now active!
echo.
echo To switch back to Hibernate, run: switch-to-hibernate.cmd
pause

