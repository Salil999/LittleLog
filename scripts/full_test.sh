#!/usr/bin/env bash

mkdir results
mkdir results/count
mkdir results/grep

./background.sh "./count_all"
./background.sh "./grep_all"