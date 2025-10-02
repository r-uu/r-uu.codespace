module de.ruu.lib.ws.rs
{
	exports de.ruu.lib.ws.rs;
	exports de.ruu.lib.ws.rs.filter.logging;

	requires transitive jakarta.ws.rs;
	requires de.ruu.lib.util;

	requires org.slf4j;
	requires static lombok;
}