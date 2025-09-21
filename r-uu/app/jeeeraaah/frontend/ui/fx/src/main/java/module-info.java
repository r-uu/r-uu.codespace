module de.ruu.app.jeeeraaah.frontend.api.client.ui.fx
{
	exports de.ruu.app.jeeeraaah.frontend.ui.fx to de.ruu.app.jeeeraaah.frontend.common.mapping;

	requires jakarta.annotation;
	requires jakarta.inject;
	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;

	requires de.ruu.lib.jpa.core;
	requires de.ruu.lib.fx.comp;
	requires de.ruu.lib.fx.core;
	requires de.ruu.lib.util;
	requires de.ruu.lib.cdi.se;
	requires de.ruu.lib.cdi.common;
	requires de.ruu.lib.mapstruct;
	requires de.ruu.lib.ws.rs;
	requires de.ruu.app.jeeeraaah.frontend.ui.fx.model;
	requires de.ruu.app.jeeeraaah.frontend.api.client.ws.rs;
	requires de.ruu.app.jeeeraaah.frontend.common.mapping;

	requires static lombok;
	requires org.slf4j;
	requires de.ruu.app.jeeeraaah.common.api.domain;
	requires de.ruu.app.jeeeraaah.common.api.ws.rs;
	requires de.ruu.app.jeeeraaah.common.api.mapping;
	requires de.ruu.app.jeeeraaah.common.api.bean;
}