module de.ruu.app.jeeeraaah.backend.api.ws.rs
{
	requires jakarta.ws.rs;
	requires de.ruu.lib.mapstruct;
	requires jakarta.inject;
	requires de.ruu.lib.ws.rs;
	requires jakarta.validation;
	requires static lombok;
	requires de.ruu.app.jeeeraaah.common.api.domain;
	requires microprofile.metrics.api;
	requires microprofile.openapi.api;
	requires de.ruu.app.jeeeraaah.backend.persistence.jpa;
	requires de.ruu.lib.jpa.core;
	requires de.ruu.app.jeeeraaah.common.api.ws.rs;
	exports de.ruu.app.jeeeraaah.backend.api.ws.rs;
}