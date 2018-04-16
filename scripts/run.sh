#!/bin/bash

cd $(pwd)/../src/
javac -cp .:$(pwd)/../lib/* littlelog/*.java
java -Xmx12G  -cp .:$(pwd)/../lib/* littlelog/Main $1 $2 $3
find . -type f -path "*/*" -name "*.class" -exec rm -f {} \;

#./grep.sh logfiles/test_compressed/ $1 | wc -l
#grep $1 ../src/logfiles/test/* | wc -l

