# fetch vps shell configuration from windows filesystem into wsl filesystem
cp -r /mnt/c/Users/${USER}/develop/github/r-uu/app/taggable/server/openliberty/config/vps ~/config/vps

dos2unix ~/config/vps/bash/*

# copy vps shell configuration to vps
scp -r ~/config/vps/bash r-uu@159.69.11.193:/home/r-uu/config/bash