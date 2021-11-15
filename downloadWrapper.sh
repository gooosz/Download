#!/bin/bash

#find youtubeToMp3 script in the "linuxWorking" dir
#cd to it
#run Makefile

SCRIPT=`find ~ -type d -name "downloadFiles" -print`
cd ${SCRIPT}
make run
cd /usr/bin
exit
