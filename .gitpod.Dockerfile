image:
  file: .gitpod.Dockerfile

tasks:
  - name: Init GraalVM & Postgres
    init: |
      echo "=== Java (GraalVM) Version ==="
      java -version
      echo "=== Native Image Availability ==="
      native-image --version || echo "native-image not installed"

      echo "=== Initializing PostgreSQL Data Directory ==="
      if [ ! -d "$PGDATA/base" ]; then
        sudo -u postgres initdb -D $PGDATA
      fi

    command: |
      echo "=== Starting PostgreSQL with pg_ctl ==="
      sudo -u postgres pg_ctl -D $PGDATA -l $PGDATA/logfile start
      sleep 2
      echo "=== Ensuring default DB and user exist ==="
      sudo -u postgres psql -c "CREATE USER gitpod WITH SUPERUSER PASSWORD 'gitpod';" || true
      sudo -u postgres psql -c "CREATE DATABASE gitpod OWNER gitpod;" || true

      echo ""
      echo "Postgres is running on port 5432"
      echo "Connect inside workspace with:"
      echo "  psql -h localhost -U gitpod -d gitpod"
