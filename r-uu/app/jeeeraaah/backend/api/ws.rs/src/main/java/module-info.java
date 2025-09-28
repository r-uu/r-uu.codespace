module de.ruu.app.jeeeraaah.backend.api.ws.rs
{
	// Jakarta EE modules
	requires transitive jakarta.cdi;
	requires jakarta.validation;
	requires jakarta.json;
	requires jakarta.persistence;
	requires jakarta.ws.rs;
	requires jakarta.inject;
	requires jakarta.xml.bind;

	// MicroProfile modules
	requires microprofile.health.api;
	requires microprofile.metrics.api;
	requires microprofile.openapi.api;
	requires microprofile.config.api;

	// Logging
	requires org.slf4j;

	// MapStruct
	requires static org.mapstruct;

	// Lombok
	requires static lombok;
	requires de.ruu.app.jeeeraaah.common.api.domain;
	requires de.ruu.lib.ws.rs;
	requires de.ruu.app.jeeeraaah.backend.persistence.jpa;
	requires de.ruu.app.jeeeraaah.common.api.ws.rs;
	requires de.ruu.lib.mapstruct;
	requires de.ruu.app.jeeeraaah.backend.common.mapping;
	requires de.ruu.lib.util;
	requires jdk.management;
	requires de.ruu.app.jeeeraaah.common.api.bean;
	requires de.ruu.lib.cdi.se;

	// Internal modules - these should be automatic modules or named modules

	// Export packages
	exports de.ruu.app.jeeeraaah.backend.api.ws.rs;
	exports de.ruu.app.jeeeraaah.backend.api.ws.rs.health;

	// Open packages for reflection
	opens de.ruu.app.jeeeraaah.backend.api.ws.rs;
	opens de.ruu.app.jeeeraaah.backend.api.ws.rs.health;
}