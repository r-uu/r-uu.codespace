module de.ruu.app.jeeeraaah.common.ee.jpa
{
	exports de.ruu.app.jeeeraaah.common.ee.jpa;

	requires de.ruu.lib.util;
	requires de.ruu.app.jeeeraaah.common;

	requires com.fasterxml.jackson.databind;
	requires jakarta.persistence;

	requires static lombok;
	requires org.slf4j;
	requires jakarta.cdi;
	requires jakarta.transaction;
}