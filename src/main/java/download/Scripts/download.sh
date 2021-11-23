#!/bin/bash
input=$1
format=$2
path_songs=$PWD"/songs/"
path_vids=$PWD"/videos/"

#echo "in bash"

#check if directory exists
if [ -d "$path_songs" ]; then
	echo "#directory /songs/ ok"
else
	mkdir songs
fi

if [ -d "$path_vids" ]; then
	echo "#directory /videos/ ok"
else
	mkdir videos
fi

#TODO don't skip clauses are skipped
if [ "$format" = "mp3" ]; then
	#download song into /songs/ directory
	youtube-dl --extract-audio --audio-format $format -o $path_songs'%(title)s.%(ext)s' $input
elif [ "$format" = "mp4" ]; then
	#download video into /videos/ directory
	if [[ "$input" =~ "youtube.com"  || "$input" =~ "youtu.be" ]]; then
		youtube-dl -f bestvideo -o $path_vids'%(title)s.%(ext)s' $input
	else
		youtube-dl -f mp4 -o $path_vids'%(title)s.%(ext)s' $input
	fi
fi



#output the filename
youtube-dl --get-title $input
