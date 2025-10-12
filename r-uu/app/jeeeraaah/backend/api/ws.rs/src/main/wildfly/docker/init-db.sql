-- PostgreSQL Database Initialization for WildFly
CREATE SCHEMA IF NOT EXISTS app_jeeeraaah_test;

-- Grant permissions to the application user
GRANT ALL PRIVILEGES ON SCHEMA app_jeeeraaah_test TO app_jeeeraaah_test;
GRANT ALL PRIVILEGES ON DATABASE app_jeeeraaah TO app_jeeeraaah_test;

-- Set default schema for the user
ALTER USER app_jeeeraaah_test SET search_path = app_jeeeraaah_test, public;
