module de.ruu.app.jeeeraaah.backend.persistence.jpa
{
	exports de.ruu.app.jeeeraaah.backend.persistence.jpa;

	requires jakarta.annotation;
	requires jakarta.cdi;
	requires jakarta.inject;
	requires jakarta.persistence;
	requires jakarta.transaction;

	requires de.ruu.lib.jpa.core;
	requires de.ruu.lib.mapstruct;
	requires de.ruu.lib.util;
	requires de.ruu.app.jeeeraaah.common.api.domain;

	requires org.slf4j;
	requires org.mapstruct;
	requires de.ruu.app.jeeeraaah.common.api.ws.rs;
    requires static lombok;

    opens de.ruu.app.jeeeraaah.backend.persistence.jpa;
	opens de.ruu.app.jeeeraaah.backend.persistence.jpa.ee;
}