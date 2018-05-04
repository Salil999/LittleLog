#!/usr/bin/env bash

# $1 = Xmb
# $2 = regex

./littlelog -i=../../logfiles/succinct_logs/out_$1 -g "$2" -t=100 | wc -l
./littlelog -i=../../logfiles/succinct_logs/out_$1 -g "$2" -t=100 | wc -l
./littlelog -i=../../logfiles/succinct_logs/out_$1 -g "$2" -t=100 | wc -l
./littlelog -i=../../logfiles/succinct_logs/out_$1 -g "$2" -t=100 | wc -l
./littlelog -i=../../logfiles/succinct_logs/out_$1 -g "$2" -t=100 | wc -l
