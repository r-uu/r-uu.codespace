@cls
@cd "%dir_root%\%root_module_name%"
@del %dir_root%\clean-install.log 2>nul
@powershell -noLogo -noProfile -command "mvn '-Dstyle.color=always' clean install | tee \"%dir_root%\clean-install.log\""