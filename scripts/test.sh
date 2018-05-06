#!/bin/bash
#
cd $(pwd)/../src/
javac -cp .:$(pwd)/../lib/* littlelog/*.java
#time java -Xmx12G -cp .:$(pwd)/../lib/* littlelog/Test
time java -Xmx12G -cp .:$(pwd)/../lib/* -agentlib:hprof=cpu=samples,depth=15,heap=sites littlelog/Test
find . -type f -path "*/*" -name "*.class" -exec rm -f {} \;