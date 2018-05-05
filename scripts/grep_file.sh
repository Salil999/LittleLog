#!/usr/bin/env bash

regex = $1
time grep "$regex" ../../logfiles/input_logs/http.log | wc -l
