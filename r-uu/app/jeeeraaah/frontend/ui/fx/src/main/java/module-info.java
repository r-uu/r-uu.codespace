module de.ruu.app.jeeeraaah.frontend.api.client.ui.fx
{
	exports de.ruu.app.jeeeraaah.frontend.ui.fx to de.ruu.app.jeeeraaah.frontend.common.mapping;

	requires de.ruu.lib.util;
	requires jakarta.annotation;
	requires javafx.base;
	requires static lombok;
	requires de.ruu.lib.jpa.core;
	requires de.ruu.app.jeeeraaah.frontend.ui.fx.model;
	requires de.ruu.lib.fx.comp;
	requires de.ruu.lib.fx.core;
	requires jakarta.inject;
	requires javafx.fxml;
	requires javafx.controls;
	requires de.ruu.lib.cdi.se;
	requires de.ruu.lib.cdi.common;
	requires de.ruu.lib.mapstruct;
	requires de.ruu.app.jeeeraaah.client.rs;
}