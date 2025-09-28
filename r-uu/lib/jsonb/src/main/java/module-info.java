module de.ruu.lib.jsonb
{
	exports de.ruu.lib.jsonb;

	requires de.ruu.lib.util;
	requires jakarta.json;
	requires jakarta.json.bind;
	requires jakarta.ws.rs;

	requires static lombok;
	requires org.slf4j;

	opens de.ruu.lib.jsonb.recursion;
}