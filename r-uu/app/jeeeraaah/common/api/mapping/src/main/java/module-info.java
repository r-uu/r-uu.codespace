module de.ruu.app.jeeeraaah.common.api.mapping
{
	exports de.ruu.app.jeeeraaah.common.api.mapping to
			de.ruu.app.jeeeraaah.common.api.domain,
			de.ruu.app.jeeeraaah.common.api.bean,
			de.ruu.app.jeeeraaah.common.api.ws.rs;

	requires de.ruu.app.jeeeraaah.common.api.domain;
	requires de.ruu.app.jeeeraaah.common.api.bean;
	requires de.ruu.app.jeeeraaah.common.api.ws.rs;
	requires de.ruu.lib.jpa.core;
	requires de.ruu.lib.mapstruct;
	requires jakarta.annotation;
	requires java.compiler;
	requires static lombok;
	requires org.mapstruct;
}