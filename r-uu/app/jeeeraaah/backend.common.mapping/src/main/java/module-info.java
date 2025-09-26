module de.ruu.app.jeeeraaah.backend.common.mapping
{
	exports de.ruu.app.jeeeraaah.backend.common.mapping          to de.ruu.app.jeeeraaah.backend.api.ws.rs, de.ruu.codespace.app.jeeeraaah.backend.api.ws.rs;
	exports de.ruu.app.jeeeraaah.backend.common.mapping.dto.jpa  to de.ruu.app.jeeeraaah.backend.api.ws.rs;
	exports de.ruu.app.jeeeraaah.backend.common.mapping.jpa.dto  to de.ruu.app.jeeeraaah.backend.api.ws.rs;
	exports de.ruu.app.jeeeraaah.backend.common.mapping.lazy.jpa to de.ruu.app.jeeeraaah.backend.api.ws.rs;
	exports de.ruu.app.jeeeraaah.backend.common.mapping.jpa.lazy;

	requires de.ruu.app.jeeeraaah.backend.persistence.jpa;
	requires de.ruu.app.jeeeraaah.common.api.ws.rs;
	requires de.ruu.lib.mapstruct;

	requires org.mapstruct;
	requires static lombok;
	requires de.ruu.lib.jpa.core;
	requires de.ruu.app.jeeeraaah.common.api.domain;
}