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
      echo "Inside workspace connect with:"
      echo "  psql -h localhost -U gitpod -d gitpod"
      echo ""
      echo "Outside workspace connect with (Gitpod forwarded port):"
      echo "  psql -h <gitpod-forwarded-host> -p 5432 -U gitpod -d gitpod"

ports:
  - port: 5432
    onOpen: notify
    visibility: public
