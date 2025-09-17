module de.ruu.app.jeeeraaah.backend.common.mapping
{
	exports de.ruu.app.jeeeraaah.backend.common.mapping;
	exports de.ruu.app.jeeeraaah.backend.common.mapping.dto.jpa;
	exports de.ruu.app.jeeeraaah.backend.common.mapping.jpa.dto;

	requires org.mapstruct;
	requires de.ruu.app.jeeeraaah.common.api.ws.rs;
	requires de.ruu.lib.mapstruct;
	requires de.ruu.app.jeeeraaah.backend.persistence.jpa;

	requires static lombok;
	requires de.ruu.lib.jpa.core;
	requires de.ruu.app.jeeeraaah.common.api.domain;
}