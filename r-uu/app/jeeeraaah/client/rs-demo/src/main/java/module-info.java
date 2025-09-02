module de.ruu.app.jeeeraaah.client.rs.demo
{
	exports de.ruu.app.jeeeraaah.client.rs.demo;
	opens   de.ruu.app.jeeeraaah.client.rs.demo;

	requires de.ruu.app.jeeeraaah.client.rs;
	requires de.ruu.app.jeeeraaah.common;
	requires de.ruu.lib.cdi.se;
	requires de.ruu.lib.util;

	requires jakarta.json;
	requires jakarta.xml.bind;

	requires static lombok;
	requires org.slf4j;
}