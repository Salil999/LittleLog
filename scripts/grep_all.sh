#!/usr/bin/env bash

mkdir grep_$1
./grep_test.sh 20 $1
./grep_test.sh 50 $1
./grep_test.sh 100 $1
./grep_test.sh 200 $1
./grep_test.sh 300 $1
./grep_test.sh 500 $1
time grep $1 ../../logfiles/input_logs/http.log > grep_$1/grep_$1_results.txt
