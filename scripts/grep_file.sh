#!/usr/bin/env bash

time grep "$regex" ../../logfiles/input_logs/http.log | wc -l