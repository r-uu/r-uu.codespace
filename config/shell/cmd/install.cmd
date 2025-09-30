@cls
@set dir_ruu_root_module=r-uu
@cd %dir_root%
@cd %dir_ruu_root_module%
@del build.log 2>nul
@powershell -NoProfile -Command "& cmd /c \"mvn -Dsurefire.useFile=false -Dfailsafe.useFile=false -DtrimStackTrace=false install\" 2>&1 | Tee-Object -FilePath 'build.log'; exit $LASTEXITCODE"
@cd %dir_root%