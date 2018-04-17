#!/usr/bin/env bash

if [ -z "$1" ]
  then
    echo "usage: ./grep_test.sh  [query]"
else
    echo "succinct results count:"
    ./grep.sh $1 logfiles/test_sharded/input1_compressed | wc -l
    ./grep.sh $1 logfiles/test_sharded/input2_compressed | wc -l

    echo ""
    echo ""

    echo "grep results count:"
    time grep      $1 ../src/logfiles/test/* | wc -l
fi