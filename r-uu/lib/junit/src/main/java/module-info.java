module de.ruu.lib.junit
{
	exports de.ruu.lib.junit;

	requires jakarta.inject;
	requires org.junit.platform.commons;
	requires org.slf4j;
	requires microprofile.config.api;
	requires de.ruu.lib.util;

	requires static lombok;
	requires org.junit.jupiter.api;
}