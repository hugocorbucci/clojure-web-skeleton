#!/usr/bin/env bash
set -e
set -o

./setup.sh
./bin/lein ring uberjar
