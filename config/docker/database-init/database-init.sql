-- Postgres Docker image automatically creates the "r_uu" database including "r_uu" role based on the values for
-- POSTGRES_DB, POSTGRES_USER and POSTGRES_PASSWORD in docker-compose.yml.

\connect r_uu;

DROP   DATABASE IF EXISTS lib;
CREATE DATABASE           lib;
\connect                  lib;
DROP   SCHEMA   IF EXISTS lib_test;
DROP   ROLE     IF EXISTS lib_test;
CREATE ROLE               lib_test WITH LOGIN PASSWORD 'lib_test';
CREATE SCHEMA             lib_test AUTHORIZATION lib_test;
--DROP   SCHEMA   IF EXISTS lib_staging;
--DROP   ROLE     IF EXISTS lib_staging;
--CREATE ROLE               lib_staging WITH LOGIN PASSWORD 'lib_staging';
--CREATE SCHEMA             lib_staging AUTHORIZATION lib_staging;
--DROP   SCHEMA   IF EXISTS lib_production;
--DROP   ROLE     IF EXISTS lib_production;
--CREATE ROLE               lib_production WITH LOGIN PASSWORD 'lib_production';
--CREATE SCHEMA             lib_production AUTHORIZATION lib_production;

\connect r_uu;

DROP   DATABASE IF EXISTS app_demo;
CREATE DATABASE           app_demo;
\connect                  app_demo;
DROP   SCHEMA   IF EXISTS app_demo_test;
DROP   ROLE     IF EXISTS app_demo_test;
CREATE ROLE               app_demo_test WITH LOGIN PASSWORD 'app_demo_test';
CREATE SCHEMA             app_demo_test AUTHORIZATION app_demo_test;
--DROP   SCHEMA   IF EXISTS app_demo_staging;
--DROP   ROLE     IF EXISTS app_demo_staging;
--CREATE ROLE               app_demo_staging WITH LOGIN PASSWORD 'app_demo_staging';
--CREATE SCHEMA             app_demo_staging AUTHORIZATION app_demo_staging;
--DROP   SCHEMA   IF EXISTS app_demo_production;
--DROP   ROLE     IF EXISTS app_demo_production;
--CREATE ROLE               app_demo_production WITH LOGIN PASSWORD 'app_demo_production';
--CREATE SCHEMA             app_demo_production AUTHORIZATION app_demo_production;

\connect r_uu;

DROP   DATABASE IF EXISTS app_jeeeraaah;
CREATE DATABASE           app_jeeeraaah;
\connect                  app_jeeeraaah;
DROP   SCHEMA   IF EXISTS app_jeeeraaah_test;
DROP   ROLE     IF EXISTS app_jeeeraaah_test;
CREATE ROLE               app_jeeeraaah_test WITH LOGIN PASSWORD 'app_jeeeraaah_test';
CREATE SCHEMA             app_jeeeraaah_test AUTHORIZATION app_jeeeraaah_test;
--DROP   SCHEMA   IF EXISTS app_jeeeraaah_staging;
--DROP   ROLE     IF EXISTS app_jeeeraaah_staging;
--CREATE ROLE               app_jeeeraaah_staging WITH LOGIN PASSWORD 'app_jeeeraaah_staging';
--CREATE SCHEMA             app_jeeeraaah_staging AUTHORIZATION app_jeeeraaah_staging;
--DROP   SCHEMA   IF EXISTS app_jeeeraaah_production;
--DROP   ROLE     IF EXISTS app_jeeeraaah_production;
--CREATE ROLE               app_jeeeraaah_production WITH LOGIN PASSWORD 'app_jeeeraaah_production';
--CREATE SCHEMA             app_jeeeraaah_production AUTHORIZATION app_jeeeraaah_production;