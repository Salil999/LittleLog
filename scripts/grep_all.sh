#!/usr/bin/env bash


function grep_succinct () {
    local shard_size=$1
    local regex=$2

    for run in {1..5}
    do
#        echo $1 $2
        ./littlelog -i=../../logfiles/succinct_logs/out_"$1" -g "$2" -t=100 | wc -l
    done
}

function grep_all () {
    local regex=$1
    mkdir grep_"$regex"

    declare -a shard_sizes=("20"
                            "50"
                            "100"
                            "200"
                            "300"
                            "500"
                            )

    for i in "${shard_sizes[@]}"
    do
       grep_succinct "$i" "$regex"
    done

    time grep "$regex "../../logfiles/input_logs/http.log > grep_"$regex"/grep_"$regex"_results.txt
}


declare -a regex_queries=(  "228.34.77.223"
                            "Dec"
                            )
for r in "${regex_queries[@]}"
do
   grep_all "$r"
done