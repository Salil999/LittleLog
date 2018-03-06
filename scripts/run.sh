#!/bin/bash

cd $(pwd)/../src/
rm littlelog/*.class
javac -cp . littlelog/*.java
java -cp . littlelog/Main
