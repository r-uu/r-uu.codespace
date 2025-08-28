# Start from the standard Gitpod full workspace image
FROM gitpod/workspace-full

# Install GraalVM JDK 24 (EA build)
RUN wget -q https://github.com/graalvm/graalvm-ce-builds/releases/download/jdk-24.0.2/graalvm-community-jdk-24_linux-x64_bin.tar.gz \
    && tar -xzf graalvm-community-jdk-24_linux-x64_bin.tar.gz -C /opt \
    && rm graalvm-community-jdk-24_linux-x64_bin.tar.gz \
    && mv /opt/graalvm-community-openjdk-* /opt/graalvm-24

# Set environment variables for GraalVM
ENV JAVA_HOME=/opt/graalvm-24
ENV PATH=$JAVA_HOME/bin:$PATH