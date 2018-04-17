#!/usr/bin/env bash

cd $(pwd)/../src/
javac -cp .:$(pwd)/../lib/* littlelog/*.java unit_tests/*.java
time java -cp .:$(pwd)/../lib/* unit_tests/Compress $1
find . -type f -path "*/*" -name "*.class" -exec rm -f {} \;
