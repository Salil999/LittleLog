#!/usr/bin/env bash


#echo "mb $1"
#echo "regex $2"
./littlelog -i=../../logfiles/succinct_logs/out_"$1" -g "$2" -t=100 | wc -l
./littlelog -i=../../logfiles/succinct_logs/out_"$1" -g "$2" -t=100 | wc -l
./littlelog -i=../../logfiles/succinct_logs/out_"$1" -g "$2" -t=100 | wc -l
./littlelog -i=../../logfiles/succinct_logs/out_"$1" -g "$2" -t=100 | wc -l
./littlelog -i=../../logfiles/succinct_logs/out_"$1" -g "$2" -t=100 | wc -l