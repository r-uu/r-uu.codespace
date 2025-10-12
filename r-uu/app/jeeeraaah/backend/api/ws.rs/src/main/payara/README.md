# Payara Server Configuration Directory Structure
src/main/payara/
├── config/
│   ├── domain.xml
│   ├── server.env
│   └── resources/
│       ├── glassfish-resources.xml
│       └── persistence-payara-eclipselink.xml
├── scripts/
│   ├── start-payara.cmd
│   ├── stop-payara.cmd
│   ├── deploy-payara.cmd
│   └── switch-to-payara.cmd
└── docker/
    ├── Dockerfile.payara
    └── docker-compose.payara.yml

