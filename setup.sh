#!/usr/bin/env bash
set -e
set -o

mkdir -p bin
mkdir -p tmp
mkdir -p lib
if [ ! -f ./bin/lein ]; then
  (curl --version > /dev/null) || ((brew --version > /dev/null) && (brew install curl > /dev/null))
  (curl --version > /dev/null) || ((apt-get --version > /dev/null) && (apt-get install curl -y > /dev/null))
  curl --silent "https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein" > bin/lein
fi
chmod +x bin/lein
./bin/lein > /dev/null
./bin/lein deps
