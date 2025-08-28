# Start from the standard Gitpod full workspace image
FROM gitpod/workspace-full

# Install GraalVM JDK 24
RUN curl -fsSL -o /tmp/graalvm.tar.gz \
      https://download.oracle.com/graalvm/24/latest/graalvm-jdk-24_linux-x64_bin.tar.gz \
    && mkdir -p /opt/graalvm-24 \
    && tar -xzf /tmp/graalvm.tar.gz -C /opt/graalvm-24 --strip-components=1 \
    && rm /tmp/graalvm.tar.gz

# Set environment variables for GraalVM
ENV JAVA_HOME=/opt/graalvm-24
ENV PATH=$JAVA_HOME/bin:$PATH

# (Optional) Install native-image
# RUN gu install native-image
