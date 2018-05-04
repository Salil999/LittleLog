#!/usr/bin/env bash

nohup ./littlelog -i=../../logfiles/succinct_logs/out_$1 -g "$2" -t=100 > nohup_grep_$1_$2.out 2>&1 &

nohup time grep "$2" ../../logfiles/input_logs/http.log > nohup_grep_$2.out 2>&1 &
