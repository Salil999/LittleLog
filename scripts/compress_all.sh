#!/usr/bin/env bash

nohup ./littlelog -c -i=../../logfiles/input_logs/http.log -o=../../logfiles/succinct_logs/out_200 -s=200 -t=10
 > nohup_200.out &

#nohup ./littlelog -c -i=../../logfiles/input_logs/http.log -o=../../logfiles/succinct_logs/out_500 -s=500 -t=10
# > nohup_500.out &
