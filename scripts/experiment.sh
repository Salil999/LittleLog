#!/usr/bin/env bash

#time ./run.sh
time grep " 200 " $(pwd)/../src/logfiles/*.log | wc -l
