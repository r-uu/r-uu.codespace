module de.ruu.app.jeeeraaah.frontend.ui.fx.model
{
	exports de.ruu.app.jeeeraaah.frontend.ui.fx.model to de.ruu.app.jeeeraaah.frontend.api.client.ui.fx, de.ruu.app.jeeeraaah.frontend.common.mapping, de.ruu.app.jeeeraaah.frontend.ui.fx;

	requires javafx.base;
	requires jakarta.annotation;

	requires de.ruu.lib.jpa.core;

	requires static lombok;
	requires de.ruu.lib.util;
	requires de.ruu.app.jeeeraaah.common.api.domain;
}
