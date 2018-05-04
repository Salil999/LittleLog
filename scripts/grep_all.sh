#!/usr/bin/env bash


function grep_succinct () {
    local regex=$1
#    echo "$regex"
    mkdir grep_regex
    ./grep_test_helper.sh 20 regex 1> grep_regex/20mb_results.txt 2>&1
    ./grep_test_helper.sh 50 regex 1> grep_regex/20mb_results.txt 2>&1
    ./grep_test_helper.sh 100 regex 1> grep_regex/20mb_results.txt 2>&1
    ./grep_test_helper.sh 200 regex 1> grep_regex/20mb_results.txt 2>&1
    ./grep_test_helper.sh 300 regex 1> grep_regex/20mb_results.txt 2>&1
    ./grep_test_helper.sh 500 regex 1> grep_regex/20mb_results.txt 2>&1
    time grep regex ../../logfiles/input_logs/http.log > grep_regex/grep_regex_results.txt
}


declare -a arr=("228.34.77.223"
                "Dec/2015"
                )

for i in "${arr[@]}"
do
   grep_succinct "$i"
done