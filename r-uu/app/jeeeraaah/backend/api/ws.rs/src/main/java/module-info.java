module de.ruu.codespace.app.jeeeraaah.backend.api.ws.rs
{
    requires de.ruu.app.jeeeraaah.backend.persistence.jpa;
    requires de.ruu.app.jeeeraaah.common.api.domain;
    requires de.ruu.app.jeeeraaah.common.api.ws.rs;
    requires de.ruu.lib.mapstruct;
    requires de.ruu.lib.ws.rs;
    requires jakarta.annotation;
    requires jakarta.cdi;
    requires jakarta.inject;
    requires jakarta.validation;
    requires jakarta.ws.rs;
    requires java.management;
    requires jdk.management;
    requires static lombok;
    requires microprofile.health.api;
    requires microprofile.metrics.api;
    requires microprofile.openapi.api;
    requires de.ruu.app.jeeeraaah.backend.common.mapping;
    requires de.ruu.lib.util;
}