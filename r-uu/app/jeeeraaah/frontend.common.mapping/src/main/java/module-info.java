module de.ruu.app.jeeeraaah.frontend.common.mapping
{
	exports de.ruu.app.jeeeraaah.frontend.common.mapping;

	// Reads common API bean types used by mappers
	requires de.ruu.app.jeeeraaah.common.api.bean;

	// MapStruct context helper used in mappers
	requires de.ruu.lib.mapstruct;

	// Optional lombok at compile time only
	requires static lombok;
	requires de.ruu.app.jeeeraaah.common.api.ws.rs;
	requires org.mapstruct;
	requires de.ruu.app.jeeeraaah.common;
	requires de.ruu.app.jeeeraaah.frontend.ui.fx.model;
}