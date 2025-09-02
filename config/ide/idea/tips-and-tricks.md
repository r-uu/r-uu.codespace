# solve idea problems like module not found ...
remove directory hierarchy under %LOCALAPPDATA%\JetBrains\
remove all .iml files in project hierarchy
run "mvn idea:idea"
idea/file/repair ide
idea/file/invalidate caches