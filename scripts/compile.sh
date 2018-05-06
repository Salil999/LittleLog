#!/usr/bin/env bash

cd $(pwd)/../src/
javac -Xlint:unchecked -cp  .:$(pwd)/../lib/* littlelog/*.java