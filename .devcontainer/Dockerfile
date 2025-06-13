# Dockerfile
FROM mcr.microsoft.com/devcontainers/base:ubuntu

# Install Java 24
RUN apt-get update && \
    apt-get install -y wget gnupg2 && \
    wget -O - https://download.java.net/java/GA/jdk24/latest/binaries/openjdk-24_linux-x64_bin.tar.gz | tar xz -C /opt && \
    ln -s /opt/jdk-24 /opt/java && \
    update-alternatives --install /usr/bin/java java /opt/java/bin/java 100 && \
    update-alternatives --install /usr/bin/javac javac /opt/java/bin/javac 100

ENV JAVA_HOME=/opt/java
ENV PATH="$JAVA_HOME/bin:$PATH"