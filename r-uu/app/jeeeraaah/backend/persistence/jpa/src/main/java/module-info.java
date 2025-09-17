module de.ruu.app.jeeeraaah.backend.persistence.jpa
{
	exports de.ruu.app.jeeeraaah.backend.persistence.jpa to
			de.ruu.app.jeeeraaah.backend.common.mapping,
			de.ruu.app.jeeeraaah.backend.api.ws.rs;

	requires jakarta.annotation;
	requires jakarta.persistence;
	requires jakarta.inject;
	requires jakarta.transaction;
	requires jakarta.cdi;

	requires de.ruu.lib.jpa.core;
	requires de.ruu.lib.mapstruct;
	requires de.ruu.lib.util;
	requires de.ruu.app.jeeeraaah.common.api.domain;

	requires static lombok;
	requires org.slf4j;
	requires jakarta.ws.rs;
	requires org.mapstruct;
	requires de.ruu.app.jeeeraaah.common.api.ws.rs;
}