#!/usr/bin/env bash

function count_succinct () {
    local regex=$1
    mkdir count_"$regex"
    ./count_succinct.sh 20 "$regex"
    ./count_succinct.sh 50 "$regex"
    ./count_succinct.sh 100 "$regex"
    ./count_succinct.sh 200 "$regex"
    ./count_succinct.sh 300 "$regex"
    ./count_succinct.sh 500 "$regex"
    ./count_file.sh "$regex"
}

declare -a arr=(
                "228.34.77.223"
                "02:32:17"
                "2023"
                "PUT"
                "GET"
                "HTTP"
                )

for i in "${arr[@]}"
do
   count_succinct "$i"
done