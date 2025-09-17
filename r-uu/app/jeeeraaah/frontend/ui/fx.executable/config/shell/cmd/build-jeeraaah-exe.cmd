set jpackage_name=jeee---RAAAH
:: remove jeee-RAAAH target directory recursively
rd %jpackage_name% /s /q
jpackage --name         %jpackage_name%                                                       ^
         --input        %dir_project%\target                                                  ^
         --main-jar     r-uu.app-jeeeraaah-client-fx-executable-0.0.1-SNAPSHOT.jar            ^
         --main-class   de.ruu.app.jeeeraaah.frontend.ui.fx.MainAppRunner                          ^
         --type         app-image                                                             ^
         --icon         %dir_project%\..\fx\src\main\resources\img\ico\vfl-bochum-200-200.ico ^
         --win-console                                                                        ^
         --vendor       r-uu
::         --java-options "--enable-preview" ^
::         --win-console ^
::         --verbose