@cls

@setlocal enableDelayedExpansion

:: ==============================
:: ensure shell-config is loaded
:: ==============================
@if not defined shell_config_loaded ( call "%~dp0\shell-config.cmd" )

:: change into the specific WAR module directory where liberty server is configured
@cd /d "%dir_root%\%maven_root_module%\app\jeeeraaah\backend\api\ws.rs"

:: ================================
:: stop any existing liberty server
:: ================================
@call mvn liberty:stop 2>nul

:: ================================
:: clear old log files
:: ================================
@type nul > "%file_liberty_log%"
@type nul > "%file_liberty_color_log%"
@type nul > "%file_liberty_error_log%"

:: ================================
:: check if tee tool is available
:: ================================
@if not exist "%tee_exe%" (
    echo WARNING: tee.exe not found at %tee_exe%
    echo Falling back to simple output redirection
    set use_simple_output=true
) else (
    set use_simple_output=false
)

:: ====================================
:: run liberty with appropriate output handling
:: ====================================
@title %0 (liberty server running)
:: stdout piped to tee → writes "%file_liberty_color_log%" and passes to sed
:: sed → removes ANSI escape sequences, saves to %file_liberty_log%
:: stderr redirected separately to %file_liberty_error_log%
@if "%use_simple_output%"=="true" (
    echo Starting Liberty server with simple output redirection
    mvn liberty:dev > "%file_liberty_color_log%" 2> "%file_liberty_error_log%"
) else (
    echo Starting Liberty server with tee output (coloured)
    :: -Dstyle.color=always → forces color output
    (
        mvn -Dstyle.color=always liberty:dev 2> "%file_liberty_error_log%"
    ) | "%tee_exe%" "%file_liberty_color_log%"
)
@title %0 (liberty server stopped)

:: =================================
:: strip ansi codes from colored log
:: =================================
@if not exist "%sed_exe%" (
    echo WARNING: sed.exe not found at %sed_exe%
    echo Copying log file without ANSI code stripping
    copy "%file_liberty_color_log%" "%file_liberty_log%" >nul
) else (
    "%sed_exe%" "s/\x1B\[[0-9;]*[a-zA-Z]//g" "%file_liberty_color_log%" > "%file_liberty_log%"
)

@endlocal