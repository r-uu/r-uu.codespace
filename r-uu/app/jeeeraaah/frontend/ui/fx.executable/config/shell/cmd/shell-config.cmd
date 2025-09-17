@echo --- %0 running
::@echo --- empty config
@set  dir_project=%cd%
@echo dir_project=%dir_project%
@set  path=%path%;%dir_project%\config\shell\cmd
@set  path=%path%;%java_home%\bin
@echo --- %0 finished