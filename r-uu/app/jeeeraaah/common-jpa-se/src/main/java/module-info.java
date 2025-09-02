module de.ruu.app.jeeeraaah.common.jpa.se
{
//	exports de.ruu.app.jeeeraaah.common.jpa.se;

	requires de.ruu.lib.cdi.common;
	requires de.ruu.lib.cdi.se;
	requires de.ruu.lib.jpa.core;
	requires de.ruu.lib.jpa.se;
	requires de.ruu.lib.jpa.se.hibernate.postgres;
	requires de.ruu.lib.mapstruct;
	requires de.ruu.lib.util;

	requires jakarta.annotation;
	requires jakarta.cdi;
	requires jakarta.inject;
	requires jakarta.el;
	requires jakarta.persistence;
	requires jakarta.ws.rs;

	requires weld.se.core;
	requires weld.core.impl;

	requires static lombok;
	requires org.slf4j;

//	opens de.ruu.app.jeeeraaah.common.jpa.se to weld.se.core, weld.core;
//	opens de.ruu.app.jeeeraaah.common.jpa.se;

	// Opens for CDI and JPA reflection
	// The base package of this module
//	opens de.ruu.app.jeeeraaah.common.jpa.se to
//			weld.core,
//			org.jboss.weld.environment.se,
//			org.hibernate.orm.core;

	opens de.ruu.app.jeeeraaah.common.jpa.se;
}