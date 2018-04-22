#!/usr/bin/env bash

if [ -z "$1" ]
  then
    echo "usage: ./grep_test.sh  [query]"
else
    echo "succinct results count:"
    ./grep.sh $1 logfiles/access_compressed

    echo ""
    echo ""

    echo "grep results count:"
    time grep      $1 ../src/logfiles/access.log
fi