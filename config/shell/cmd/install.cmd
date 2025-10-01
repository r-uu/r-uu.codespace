@cls
@cd  %dir_root%ß%root_module_name%
@del build.log 2>nul
@powershell -noLogo -noProfile -command "mvn '-Dstyle.color=always' install | tee build.log"