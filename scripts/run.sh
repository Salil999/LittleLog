#!/bin/bash

./grep.sh logfiles/test_compressed/ $1 | wc -l
grep $1 ../src/logfiles/test/* | wc -l
