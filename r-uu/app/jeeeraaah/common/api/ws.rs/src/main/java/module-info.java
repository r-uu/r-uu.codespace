module de.ruu.app.jeeeraaah.common.api.ws.rs
{
	exports de.ruu.app.jeeeraaah.common.api.ws.rs to
			de.ruu.app.jeeeraaah.backend.common.mapping,
			de.ruu.app.jeeeraaah.frontend.common.mapping,
			de.ruu.app.jeeeraaah.common.api.domain,
			de.ruu.app.jeeeraaah.backend.api.ws.rs,
			de.ruu.app.jeeeraaah.common.api.mapping, de.ruu.app.jeeeraaah.backend.persistence.jpa;

	requires de.ruu.app.jeeeraaah.common.api.domain;

	requires com.fasterxml.jackson.annotation;
	requires jakarta.annotation;

	requires static lombok;
	requires de.ruu.lib.util;
	requires de.ruu.lib.jpa.core;
}