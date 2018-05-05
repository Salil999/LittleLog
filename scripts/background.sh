#!/usr/bin/env bash

nohup "$@" > nohup_$1.out 2>&1 &