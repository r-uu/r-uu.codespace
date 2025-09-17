@echo --- %0 running
@title jeeeraaah server codespace
::@echo --- shell config - calling
@call .\config\shell\cmd\shell-config.cmd
::@echo --- shell config - returned
cls && mvn liberty:dev
@echo --- %0 finished