#!/usr/bin/env bash

echo uncompression
time gunzip $2.gz

echo
echo grep line count
time grep $1 $2 | wc -l

echo
echo compression
time gzip $2

echo
echo total
