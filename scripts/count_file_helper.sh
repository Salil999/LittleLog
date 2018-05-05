#!/usr/bin/env bash

time grep -o $1 ../../logfiles/input_logs/http.log | wc -l