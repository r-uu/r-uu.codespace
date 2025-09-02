module de.ruu.app.jeeeraaah.common
{
	exports de.ruu.app.jeeeraaah.common;
	exports de.ruu.app.jeeeraaah.common.bean;
	exports de.ruu.app.jeeeraaah.common.dto;
	exports de.ruu.app.jeeeraaah.common.fx;
	exports de.ruu.app.jeeeraaah.common.jpa;
	exports de.ruu.app.jeeeraaah.common.map;

	requires de.ruu.lib.mapstruct;
	requires de.ruu.lib.util;
	requires de.ruu.lib.ws.rs;
	requires com.fasterxml.jackson.annotation;
	requires de.ruu.lib.jpa.core;
	requires jakarta.annotation;
	requires jakarta.persistence;
	requires jakarta.ws.rs;
	requires java.compiler;
	requires javafx.base;
	requires org.mapstruct;

	requires org.slf4j;
	requires static lombok;
}