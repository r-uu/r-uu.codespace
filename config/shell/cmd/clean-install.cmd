@cls
@setlocal
@set "root_module_name=r-uu"
@cd "%dir_root%\%root_module_name%"
@del build.log 2>nul
@powershell -NoProfile -Command "& cmd /c \".\\mvnw.cmd -Dsurefire.useFile=false -Dfailsafe.useFile=false -DtrimStackTrace=false clean install\" 2>&1 | Tee-Object -FilePath 'build.log'; exit $LASTEXITCODE"
@endlocal
