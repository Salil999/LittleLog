#!/bin/bash

cd $(pwd)/../src/
javac -cp .:$(pwd)/../lib/succinct-0.1.6.jar littlelog/*.java
java -cp .:$(pwd)/../lib/succinct-0.1.6.jar littlelog/Main
find . -type f -path "*/*" -name "*.class" -exec rm -f {} \;