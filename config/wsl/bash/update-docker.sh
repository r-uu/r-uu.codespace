# fetch docker configuration from windows filesystem into wsl filesystem
cp -r /mnt/c/Users/${USER}/develop/github/ruu/config/docker ~/config

dos2unix ~/config/docker/*
dos2unix ~/config/docker/.*
dos2unix ~/config/docker/database-env/*
dos2unix ~/config/docker/database-init/*