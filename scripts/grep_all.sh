#!/usr/bin/env bash

function grep_succinct () {
    local regex=$1
    mkdir grep_"$regex"
    grep_succinct.sh 20 "$regex"
    grep_succinct.sh 50 "$regex"
    grep_succinct.sh 100 "$regex"
    grep_succinct.sh 200 "$regex"
    grep_succinct.sh 300 "$regex"
    grep_succinct.sh 500 "$regex"
    grep_file.sh "$regex"
}

declare -a arr=("228.34.77.223"
#                "Dec/2015"
                )

for i in "${arr[@]}"
do
   grep_succinct "$i"
done