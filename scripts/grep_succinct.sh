#!/usr/bin/env bash

# $1 = Xmb
# $2 = regex
./grep_succinct_helper.sh $1 $2 1> grep_$2/$1mb_results.txt 2>&1