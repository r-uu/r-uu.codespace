module de.ruu.app.jeeeraaah.common.jpa.ee
{
	exports de.ruu.app.jeeeraaah.common.jpa.ee;

	requires de.ruu.lib.util;
	requires de.ruu.app.jeeeraaah.common;

	requires com.fasterxml.jackson.databind;
	requires jakarta.persistence;

	requires static lombok;
	requires org.slf4j;
	requires org.apache.logging.log4j;
	requires jakarta.cdi;
	requires jakarta.transaction;
}