#!/usr/bin/env bash

time grep $1 ../../logfiles/input_logs/http.log | wc -l