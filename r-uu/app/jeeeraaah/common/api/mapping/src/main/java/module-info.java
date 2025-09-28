module de.ruu.app.jeeeraaah.common.api.mapping
{
	exports de.ruu.app.jeeeraaah.common.api.mapping to
			de.ruu.app.jeeeraaah.common.api.domain,
			de.ruu.app.jeeeraaah.common.api.bean,
			de.ruu.app.jeeeraaah.common.api.ws.rs,
			de.ruu.app.jeeeraaah.frontend.api.client.ws.rs,
			de.ruu.app.jeeeraaah.frontend.ui.fx;
	exports de.ruu.app.jeeeraaah.common.api.mapping.bean.dto to org.mapstruct;
	exports de.ruu.app.jeeeraaah.common.api.mapping.dto.bean to org.mapstruct;
	exports de.ruu.app.jeeeraaah.common.api.mapping.lazy.bean to org.mapstruct;

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