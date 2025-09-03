FROM gitpod/workspace-full:latest

# ---- GraalVM installieren ----
USER root
RUN wget -qO- https://download.oracle.com/graalvm/24/latest/graalvm-jdk-24_linux-x64_bin.tar.gz \
    | tar xvz -C /opt/ \
    && ln -s /opt/graalvm-* /opt/graalvm

ENV JAVA_HOME=/opt/graalvm
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# ---- Postgres installieren ----
RUN apt-get update && apt-get install -y --no-install-recommends \
      postgresql postgresql-contrib \
    && rm -rf /var/lib/apt/lists/*

ENV PGDATA=/var/lib/postgresql/data
RUN mkdir -p $PGDATA && chown -R postgres:postgres $PGDATA

USER gitpod