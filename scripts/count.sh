#!/bin/bash

cd $(pwd)/../src/
javac -cp .:$(pwd)/../lib/* littlelog/*.java unit_tests/*.java
time java -Xmx12G  -cp .:$(pwd)/../lib/* unit_tests/Count $1 $2
find . -type f -path "*/*" -name "*.class" -exec rm -f {} \;
