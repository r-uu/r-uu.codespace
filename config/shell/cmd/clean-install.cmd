@cls
@cd "%dir_root%\%root_module_name%"
@del build.log 2>nul
@powershell -noLogo -noProfile -command "mvn '-Dstyle.color=always' clean install | tee build.log"