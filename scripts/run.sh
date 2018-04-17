#!/bin/bash
#
cd $(pwd)/../src/
javac -cp .:$(pwd)/../lib/* littlelog/*.java
java -Xmx12G  -cp .:$(pwd)/../lib/* littlelog/Main $1 $2 $3
find . -type f -path "*/*" -name "*.class" -exec rm -f {} \;

