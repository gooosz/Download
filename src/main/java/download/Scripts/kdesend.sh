#!/bin/bash

# console colors 2020-08-22 12:47:37
GREEN='\033[0;32m'
LGREEN='\033[1;32m'
WHITE='\033[1;37m'
YELL='\033[1;33m'
RED='\033[0;31m'
LRED='\033[1;31m'
MAG='\033[0;35m'
LMAG='\033[1;35m'
CYAN='\033[0;36m'
LCYAN='\033[1;36m'
NC='\033[0m' # No Color

filename="$1"
pathname=$(dirname "$1")
basename=$(basename "$1")
extension="${filename##*.}"
name_no_ext="${basename%.*}"


#kdesend command line tool, version 09-03-2019

#check variable existence
if [ -z "$1" ]; then
  echo 'Indiquez un fichier à envoyer'
exit
fi


#check if absolute or relative path
if [[ "$1" = /* ]]; then
   filename=$1
else
   filename="$PWD/songs/$1"
fi


#echo $filename>/home/boony/test.txt
#echo $PWD>>/home/boony/test.txt

#echo "sending file $filename"
deviceid=""
configfile=~/.config/kdesend.conf
#Problème quand il y a plus d'un device vu le 2021-02-20 13:53:35
ids=$(kdeconnect-cli -a --id-only)
list=$(kdeconnect-cli -a)
echo "Available devices : "
echo "${list[@]}"
if [ -f "$configfile" ];
then
   echo "config file found "
   deviceid=$(cat $configfile)
   printf "deviceid=${YELL} $deviceid ${NC}\n"
fi

#compter le nombre de devices availables (nombre de lignes renvoyées)
nbr_devices=$(echo "${list[@]}" | wc -l)
#Comparaison différent de 1
if [ -z "$deviceid" ]; then
   echo "There is no single device!"
   printf "${LRED}Please set device id in config file ${YELL}'$configfile'${NC}\nRestart application\n"
   echo "${ids[@]}"> "$configfile"
   read #continuer vers le fichier config
   nano "$configfile"

else
printf "OK : device from config : \n [${WHITE} $deviceid ${NC}]\n"

result=$(kdeconnect-cli -d $deviceid --share "$filename")

zenity --notification --text "\n$result" --timeout 1

fi
