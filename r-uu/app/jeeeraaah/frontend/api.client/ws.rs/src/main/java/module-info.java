module de.ruu.app.jeeeraaah.frontend.api.client.ws.rs
{
	exports de.ruu.app.jeeeraaah.frontend.ws.rs;
	exports de.ruu.app.jeeeraaah.frontend.ws.rs.dto;

	opens de.ruu.app.jeeeraaah.frontend.ws.rs;
	opens de.ruu.app.jeeeraaah.frontend.ws.rs.dto;

	// Jackson
	requires com.fasterxml.jackson.annotation;
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.jakarta.rs.json;

	// Jakarta EE
	requires jakarta.activation;
	requires jakarta.annotation;
	requires jakarta.cdi;
	requires jakarta.el;
	requires jakarta.inject;
	requires jakarta.json;
	requires jakarta.ws.rs;
	requires jakarta.xml.bind;

	// MicroProfile
	requires microprofile.config.api;

	// Internal libraries
	requires de.ruu.lib.cdi.common;
	requires de.ruu.lib.cdi.se;
	requires de.ruu.lib.jackson;
	requires de.ruu.lib.mapstruct;
	requires de.ruu.lib.util;
	requires de.ruu.lib.ws.rs;

	// Application modules
	requires de.ruu.app.jeeeraaah.common.api.bean;
	requires de.ruu.app.jeeeraaah.common.api.domain;
	requires de.ruu.app.jeeeraaah.common.api.ws.rs;

	// Development tools
	requires static lombok;
	requires org.slf4j;
	requires de.ruu.app.jeeeraaah.common.api.mapping;
}