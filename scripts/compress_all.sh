#!/usr/bin/env bash

nohup ./littlelog -c -i=../../logfiles/input_logs/test.txt -o=../../logfiles/succinct_logs/out_test -s=200 -t=10
 > nohup_200.out 2>&1 &