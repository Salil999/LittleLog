#!/usr/bin/env bash

python3 lint.py "$1"/ > "$1".txt; python3 results.py "$1".txt
