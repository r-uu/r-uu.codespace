module de.ruu.app.jeeeraaah.client.rs
{
	exports de.ruu.app.jeeeraaah.client.ws.rs;
	exports de.ruu.app.jeeeraaah.client.ws.rs.dto;

	opens de.ruu.app.jeeeraaah.client.ws.rs;
	opens de.ruu.app.jeeeraaah.client.ws.rs.dto;

	requires com.fasterxml.jackson.annotation;
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.jakarta.rs.json;
	requires jakarta.activation;
	requires jakarta.annotation;
	requires jakarta.cdi;
	requires jakarta.el;
	requires jakarta.inject;
	requires jakarta.json;
	requires jakarta.ws.rs;
	requires jakarta.xml.bind;
	requires microprofile.config.api;

	requires de.ruu.app.jeeeraaah.common;
	requires de.ruu.lib.util;
	requires de.ruu.lib.jackson;

	requires static lombok;
	requires org.slf4j;
	requires de.ruu.lib.ws.rs;
	requires de.ruu.lib.mapstruct;
	requires de.ruu.lib.cdi.common;
	requires javafx.base;
	requires de.ruu.lib.cdi.se;
}