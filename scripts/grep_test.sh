#!/usr/bin/env bash

./littlelog -i=../../logfiles/succinct_logs/out_$1 -g "$2" -t=100 >> nohup_grep_$1_$2.out
./littlelog -i=../../logfiles/succinct_logs/out_$1 -g "$2" -t=100 >> nohup_grep_$1_$2.out
./littlelog -i=../../logfiles/succinct_logs/out_$1 -g "$2" -t=100 >> nohup_grep_$1_$2.out

#nohup ./littlelog -i=../../logfiles/succinct_logs/out_$1 -g "$2" -t=100 > nohup_grep_$1_$2.out 2>&1 &
#
#nohup ./littlelog -i=../../logfiles/succinct_logs/out_$1 -g "$2" -t=100 > nohup_grep_$1_$2.out 2>&1 &
#
#nohup ./littlelog -i=../../logfiles/succinct_logs/out_$1 -g "$2" -t=100 > nohup_grep_$1_$2.out 2>&1 &