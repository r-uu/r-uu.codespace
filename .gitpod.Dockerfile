FROM gitpod/workspace-full

# Install GraalVM JDK 24 into /home/gitpod/.graalvm
RUN curl -fsSL -o /tmp/graalvm.tar.gz \
      https://download.oracle.com/graalvm/24/latest/graalvm-jdk-24_linux-x64_bin.tar.gz \
    && mkdir -p /home/gitpod/.graalvm/graalvm-24 \
    && tar -xzf /tmp/graalvm.tar.gz -C /home/gitpod/.graalvm/graalvm-24 --strip-components=1 \
    && rm /tmp/graalvm.tar.gz

# Set environment variables for GraalVM
ENV JAVA_HOME=/home/gitpod/.graalvm/graalvm-24
ENV PATH=$JAVA_HOME/bin:$PATH

# Optional: Install native-image
# RUN gu install native-image