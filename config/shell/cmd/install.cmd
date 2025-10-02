@cls
@cd  %dir_root%ß%root_module_name%
@del %dir_root%\install.log 2>nul
@powershell -noLogo -noProfile -command "mvn '-Dstyle.color=always' clean install | tee \"%dir_root%\install.log\""