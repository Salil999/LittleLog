#!/usr/bin/env bash

cd $(pwd)/../src/
javac -cp .:$(pwd)/../lib/* littlelog/*.java unit_tests/*.java
time java -Xmx12G  -cp .:$(pwd)/../lib/* unit_tests/Shard $1 $2 $3
find . -type f -path "*/*" -name "*.class" -exec rm -f {} \;
