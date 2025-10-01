@cls
@set dir_jeeeraaah_server_module=r-uu\app\jeeeraaah\backend\api\ws.rs
@cd %dir_root%
@cd %dir_jeeeraaah_server_module%
:: provide resources for liberty and then start liberty in dev mode
@mvn -B -DskipTests process-resources && mvn liberty:dev
:: start liberty in dev mode
::@mvn liberty:dev
:: back to the root
@cd %dir_root%