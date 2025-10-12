# Project Prompt Template

This template defines the context, rules and goals for working on this Java Enterprise Software Development project.  
It ensures consistent responses and code modifications across tools like ChatGPT, Claude, or GitHub Copilot.

---------------------------------------------------------------------

## Context

### General purpose
  - Explore how to make benefit from consequent usage of JPMS throughout a Jakarta Enterprise 10 project
  - leverage very modern software technology stack
### Domain
  - Management of tasks and taskgroups
### Tech Stack
- Java version: 24
- Operating System: Windows 11
- Shell scripting: cmd, no powershell if possible
- Build tool: Maven, no Gradle
- Version control: Git (GitHub)
- Enterprise frameworks:
    - Jakarta EE 10
    - MicroProfile 6
    - CDI
    - JPA
      - hibernate
      - eclipselink
    - JAX-RS
    - JavaFX
- Application servers:
    - Open Liberty
    - Payara
    - Wildfly
- Database
  - Postgresql running in docker container under ubuntu wsl.
- Docker
  - Use docker-compose inside wsl ubuntu.
  - Make postgres start automatically in a docker container when starting ubuntu wsl.
- IDE: IntelliJ IDEA Community

---------------------------------------------------------------------

## Rules

### Maven
- Use maven multi module project.
- Always use Java 24.
- Do not use <properties> for dependency/plugin versions.
- All dependency versions must be managed in the root POM under <dependencyManagement>.
- All plugin versions must be managed in the root POM under <pluginManagement>.
- Centralize build configuration in the root POM whenever possible.
- Sort dependencies by artifactId.
- Remove unused dependencies.

### Java
- Derive coding standards from the existing project code and preserve them in all changes.
- Ensure consistent JPMS (Java Platform Module System) usage across the whole project.
- Remove unused imports in .java files.
- Remove unused requires in module-info.java files.
- Keep requires minimal, remove unnecessary opens statements, remove unnecessary transitive.
- Sort imports, requires and opens alphabetically.

### General
- All comments must be in English.
- Replace existing German comments with English comments.

### Build
- Root directory is "codespace".
- The Maven root module is "r-uu" inside "codespace".
- Builds must be started via:  
  codespace\config\shell\clean-install.cmd

### Liberty
- Always use the configured PostgreSQL database.
- To start the Liberty server, use:  
  codespace\config\shell\lib-dev.cmd

---------------------------------------------------------------------

## Goals

---------------------------------------------------------------------

## Output Requirements
- Always return full working code blocks (cmd, xml, java, etc.) in Markdown.
- Explanations in English, unless explicitly requested in German.
- When fixing errors, show the corrected file in full (not only fragments).

---------------------------------------------------------------------

# Actual tasks
- start with the creation of a git repository named "jakarta-enterprise-software-development"
- create directories inside the root of the repository
  - config/ai-prompt
  - config/docker
  - config/shell/cmd
  - config/shell/wsl
  - config/wsl/bash