#!/usr/bin/env bash

mkdir results
mkdir results/count
mkdir results/grep

./background.sh "./count_all.sh"
./background.sh "./grep_all.sh"