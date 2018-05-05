#!/usr/bin/env bash

cd $(pwd)/../src/
find . -type f -path "*/*" -name "*.class" -exec rm -f {} \;