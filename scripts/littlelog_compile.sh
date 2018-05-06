#!/usr/bin/env bash

cd $(pwd)/../src/
javac -Xlint:unchecked -cp  .:$(pwd)/../lib/* littlelog/*.java
time java -Xmx40G  -cp .:$(pwd)/../lib/* littlelog/Main "$@"
find . -type f -path "*/*" -name "*.class" -exec rm -f {} \;