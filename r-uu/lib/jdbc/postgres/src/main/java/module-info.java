module de.ruu.lib.jdbc.postgres
{
	exports de.ruu.lib.jdbc.postgres;

	requires java.naming;
	requires java.sql;
	requires static lombok;
	requires org.postgresql.jdbc;
	requires de.ruu.lib.jdbc.core;
}