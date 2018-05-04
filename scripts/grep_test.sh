#!/usr/bin/env bash

# $1 = Xmb
# $2 = regex

X = $1
regex = $2

./grep_test_helper.sh MB regex 1> grep_regex/Xmb_results.txt 2>&1