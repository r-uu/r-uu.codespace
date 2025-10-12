@echo --- %0 running

:: Prevent reloading
@if defined shell_config_loaded goto :eof
@set shell_config_loaded=1

:: %~dp0 expands to the directory (d=drive, p=path (including trailing \), 0=file) of this file
@set "dir_root=%~dp0..\..\.."

@set maven_root_module=r-uu
:: paths to tee.exe, sed.exe and bash.exe from git installation
@set dir_git=c:\program files\git
@set dir_git_bin=%dir_git%\usr\bin
@set tee_exe=%dir_git_bin%\tee.exe
@set sed_exe=%dir_git_bin%\sed.exe
@set bash_exe=%dir_git_bin%\bash.exe

@set file_maven_log=%dir_root%\maven.log
@set file_maven_color_log=%dir_root%\maven-color.log
@set file_maven_error_log=%dir_root%\maven-error.log

@set file_liberty_log=%dir_root%\liberty.log
@set file_liberty_color_log=%dir_root%\liberty-color.log
@set file_liberty_error_log=%dir_root%\liberty-error.log

@path=%path%;%dir_root%\config\shell\cmd

@echo --- %0 finished