##!/usr/bin/env bash
#
#
#function grep_succinct () {
#    local shard_size=$1
#    local regex=$2
#
#    ./grep_test_helper.sh "$shard_size" "$regex" 1> grep_"$regex"/"$shard_size"mb_results.txt 2>&1
#}
#
#function grep_all () {
#    local regex=$1
#    mkdir grep_"$regex"
#
#    declare -a shard_sizes=("20"
#                            "50"
#                            "100"
#                            "200"
#                            "300"
#                            "500"
#                            )
#
#    for s in "${shard_sizes[@]}"
#    do
#       grep_succinct "$s" "$regex"
#    done
#
##    time grep "$regex "../../logfiles/input_logs/http.log > grep_"$regex"/grep_"$regex"_results.txt
#}
#
#
#declare -a regex_queries=(  "228.34.77.223"
#                            "Dec"
#                            )
#for r in "${regex_queries[@]}"
#do
#   grep_all "$r"
#done

#!/usr/bin/env bash


function grep_succinct () {
    local regex=$1
    mkdir grep_"$regex"
    ./grep_test_helper.sh 20 regex 1> grep_"$regex"/20mb_results.txt 2>&1
    ./grep_test_helper.sh 50 regex 1> grep_"$regex"/50mb_results.txt 2>&1
    ./grep_test_helper.sh 100 regex 1> grep_"$regex"/100mb_results.txt 2>&1
    ./grep_test_helper.sh 200 regex 1> grep_"$regex"/200mb_results.txt 2>&1
    ./grep_test_helper.sh 300 regex 1> grep_"$regex"/300mb_results.txt 2>&1
    ./grep_test_helper.sh 500 regex 1> grep_"$regex"/500mb_results.txt 2>&1
    time grep "$regex "../../logfiles/input_logs/http.log > grep_"$regex"/grep_"$regex"_results.txt
}


declare -a arr=("228.34.77.223"
                "Dec/2015"
                )

for i in "${arr[@]}"
do
   grep_succinct "$i"
done