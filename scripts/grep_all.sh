#!/usr/bin/env bash


function grep_succinct () {
    local shard_size=$1
    local regex=$2

    for run in {1..5}
    do
        ./grep_test_helper.sh "$shard_size" "$regex" 1> grep_"$regex"/"$shard_size"mb_results.txt 2>&1
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

    for s in "${shard_sizes[@]}"
    do
       grep_succinct "$s" "$regex"
    done

#    time grep "$regex "../../logfiles/input_logs/http.log > grep_"$regex"/grep_"$regex"_results.txt
}


declare -a regex_queries=(  "228.34.77.223"
                            "Dec"
                            )
for r in "${regex_queries[@]}"
do
   grep_all "$r"
done