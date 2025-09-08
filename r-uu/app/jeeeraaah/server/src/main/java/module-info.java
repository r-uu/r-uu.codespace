module de.ruu.app.jeee_raaa.server
{
	requires de.ruu.app.jeeeraaah.common;
	requires de.ruu.lib.jackson;
	requires de.ruu.lib.util;

	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.jakarta.rs.json;
	requires jakarta.cdi;
	requires jakarta.ws.rs;
	requires jdk.management;
	requires microprofile.health.api;
	requires microprofile.metrics.api;
	requires microprofile.openapi.api;

	requires static lombok;
	requires org.slf4j;
	requires de.ruu.lib.mapstruct;
	requires jakarta.inject;
	requires de.ruu.lib.ws.rs;
	requires jakarta.validation;
}