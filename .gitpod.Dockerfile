# Verwende ein offizielles GraalVM-Image als Basis
FROM ghcr.io/graalvm/jdk-community:24

# Setze die Umgebungsvariable für JAVA_HOME
ENV JAVA_HOME /opt/graalvm