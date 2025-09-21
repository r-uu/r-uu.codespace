module de.ruu.app.jeeeraaah.common.api.ws.rs
{
    // Export the package to any module that needs it
    exports de.ruu.app.jeeeraaah.common.api.ws.rs;

    requires de.ruu.app.jeeeraaah.common.api.domain;

    requires com.fasterxml.jackson.annotation;
    requires jakarta.annotation;

    requires static lombok;
    requires de.ruu.lib.util;
    requires de.ruu.lib.jpa.core;
}