@cls

@setlocal enableDelayedExpansion

:: ==============================
:: ensure shell-config is loaded
:: ==============================
@if not defined shell_config_loaded ( call "%~dp0\shell-config.cmd" )

:: Change into Maven root module directory
@cd /d "%dir_root%\%maven_root_module%"

:: ================================
:: clear old log files
:: ================================
@type nul > "%file_maven_log%"
@type nul > "%file_maven_color_log%"
@type nul > "%file_maven_error_log%"

@title %0 (build running)
:: ==================================
:: run maven with live colored output
:: ==================================
:: -Dstyle.color=always → forces color output
:: stdout piped to tee → writes "%file_maven_color_log%" and passes to sed
:: sed → removes ANSI escape sequences, saves to %file_maven_log%
:: stderr redirected separately to %file_maven_error_log%
(
    mvn -Dstyle.color=always clean install 2> "%file_maven_error_log%"
) | "%tee_exe%" "%file_maven_color_log%"

:: =================================
:: strip ansi codes from colored log
:: =================================
"%sed_exe%" "s/\x1B\[[0-9;]*[a-zA-Z]//g" "%file_maven_color_log%" > "%file_maven_log%"
@title %0 (build finished)

@endlocal