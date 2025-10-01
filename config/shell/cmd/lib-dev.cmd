@cls
@set dir_jeeeraaah_server_module=r-uu\app\jeeeraaah\backend\api\ws.rs
@cd %dir_root%
@cd %dir_jeeeraaah_server_module%
:: laufenden Liberty-Server sicher stoppen, um Konflikte mit dev mode zu vermeiden
@call mvn -B -q liberty:stop
:: make sure resources for liberty are provided
@call mvn -B -DskipTests process-resources
:: start liberty in dev mode (ohne ANSI-Codes) und schreibe Log zentral nach r-uu/build.log
@call powershell -noLogo -noProfile -command "mvn '-Dstyle.color=always' liberty:dev | tee \"%dir_root%\build.log\""
:: back to the root
@cd %dir_root%