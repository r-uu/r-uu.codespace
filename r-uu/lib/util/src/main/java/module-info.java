module de.ruu.lib.util
{
	exports de.ruu.lib.util;
	exports de.ruu.lib.util.classpath;
	exports de.ruu.lib.util.lang.model;
	exports de.ruu.lib.util.rs;
	exports de.ruu.lib.util.json;
	exports de.ruu.lib.util.bimapped;

	requires static lombok;

	requires jakarta.annotation;
	requires jakarta.ws.rs;
	requires java.compiler;
	requires org.slf4j;
}