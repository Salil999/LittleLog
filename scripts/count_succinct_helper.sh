#!/usr/bin/env bash

#echo "mb $1"
#echo "regex $2"
./littlelog -i=../../logfiles/succinct_logs/out_"$1" -n "$2" -t=100 -l=10
#./littlelog -i=../../logfiles/succinct_logs/out_"$1" -n "$2" -t=100 -l=10
#./littlelog -i=../../logfiles/succinct_logs/out_"$1" -n "$2" -t=100 -l=10