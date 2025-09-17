module de.ruu.app.jeeeraaah.common.api.bean
{
	exports de.ruu.app.jeeeraaah.common.api.bean;

	requires de.ruu.app.jeeeraaah.common.api.domain;

	requires de.ruu.lib.jpa.core;
	requires de.ruu.lib.mapstruct;
	requires de.ruu.lib.util;

	requires jakarta.annotation;

	requires static lombok;
	requires org.slf4j;
}
