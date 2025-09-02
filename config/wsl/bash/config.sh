#!/bin/bash

echo starting bash configuration

# docker command aliases for taggable server
alias docker-daemon-start="sudo service docker start"
alias docker-daemon-stop=" sudo service docker stop"
alias docker-system-prune="sudo docker system prune --all --volumes --force && sudo docker volume prune --all --force"

alias cd-config-docker="cd ~/config/docker"

#alias taggable-delete="docker image rm openliberty"
#alias taggable-build="cd-config-docker && docker build . -t taggable:0.0.1-SNAPSHOT"
#alias taggable-start="cd-config-docker && docker-compose up -d"
#alias taggable-stop=" cd-config-docker && docker-compose down"

alias postgres-start="\
     cd-config-docker \
  && docker-compose up -d postgres"
alias postgres-stop="docker container stop postgres"

# clears console and starts new shell
# new shell will source this file from .bashrc to make shell configuration updates available immediately
alias shell-reset="clear && exec $SHELL"

#alias ssh-vps="ssh r-uu@159.69.11.193"

alias update-docker="chmod +x ~/config/wsl/bash/update-docker.sh && ~/config/wsl/bash/update-docker.sh"
#alias update-vps="   chmod +x ~/config/wsl/bash/update-vps.sh    && ~/config/wsl/bash/update-vps.sh"

echo "run shell-reset to make updates in config.sh available"

echo finished  bash configuration

#echo "mounting onedrive: unmount directory (if mounted already)"
#fusermount -uz ~/mount/onedrive
#echo "mounting onedrive: create  directory (if not already present)"
#mkdir -p ~/mount/onedrive
#echo "mounting onedrive: mount   directory (in background process)"
#rclone --vfs-cache-mode writes mount "onedrive":  ~/mount/onedrive &
#echo "mounting onedrive: finished"