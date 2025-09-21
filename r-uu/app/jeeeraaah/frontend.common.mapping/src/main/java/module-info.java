module de.ruu.app.jeeeraaah.frontend.common.mapping
{
	exports de.ruu.app.jeeeraaah.frontend.common.mapping;

	// MapStruct context helper used in mappers
	requires de.ruu.lib.mapstruct;

	// Reads common API bean types used by mappers
	requires de.ruu.app.jeeeraaah.common.api.bean;
	requires de.ruu.app.jeeeraaah.common.api.ws.rs;
	requires de.ruu.app.jeeeraaah.frontend.ui.fx.model;

	// Optional lombok at compile time only
	requires static lombok;
	requires org.mapstruct;
	requires de.ruu.lib.jpa.core;
}