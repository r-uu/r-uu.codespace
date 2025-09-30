# r-uu Java 24 Build

Dieses Modul-Root wurde auf Java 24 umgestellt. Alle Abhängigkeits- und Pluginversionen werden zentral im Root-`pom.xml` verwaltet (ohne Versions-Properties). Zum Bauen wird der Maven Wrapper verwendet, daher ist keine globale Maven-Installation nötig.

## Voraussetzungen
- JDK 24 (JAVA_HOME gesetzt, `"%JAVA_HOME%\bin"` im PATH)
- Internetzugang (einmaliger Download des Maven Wrapper JAR und der Maven-Distribution)

## Schnellstart (Windows)
1. Java 24 prüfen:
   ```bat
   java -version
   ```
   Ausgabe sollte Version 24.x anzeigen. Falls nicht, JAVA_HOME setzen, z. B.:
   ```bat
   set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-24"
   set "PATH=%JAVA_HOME%\bin;%PATH%"
   ```
2. Maven Wrapper prüfen:
   ```bat
   mvnw.cmd -v
   ```
3. Bauen ohne Tests (schnell):
   ```bat
   mvnw.cmd -DskipTests clean package
   ```
4. Voller Build mit Tests:
   ```bat
   mvnw.cmd clean install
   ```

## Hinweise zur Projektkonfiguration
- Java: `maven-compiler-plugin` ist zentral mit `source/target/release=24` konfiguriert.
- Tests: `maven-surefire-plugin` ist zentral konfiguriert (`useModulePath=false`, notwendige `--add-opens`).
- Annotation Processing: `lombok`, `lombok-mapstruct-binding`, `mapstruct-processor`, `hibernate-jpamodelgen` zentral im Root hinterlegt.
- Dependencies: Externe Abhängigkeiten sind im `dependencyManagement` des Root-POM alphabetisch nach `artifactId` sortiert; Module deklarieren nur `groupId`/`artifactId` (ohne Versionen).

## Troubleshooting
- "java" wird nicht gefunden: JAVA_HOME setzen und PATH ergänzen, anschließend `java -version` prüfen.
- Firewall/Proxy blockiert Downloads: ggf. Proxy-Konfiguration für `powershell`, `curl` oder `wget` anpassen.
- Erstes `mvnw.cmd` dauert: Es werden Wrapper JAR und Maven 3.9.9 geladen.


