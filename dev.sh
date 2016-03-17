#!/usr/bin/env bash
set -e
set -o

./setup.sh
(mongod --version &> /dev/null) || ( (brew --version &> /dev/null) && (brew install mongodb > /dev/null) )
(mongod --version &> /dev/null) || ( (apt-get --version &> /dev/null) && (apt-get install -y mongodb-org > /dev/null) )
GAFFER_VERSION='gaffer-0.0.1'
if [ ! -f ./bin/gaffer ]; then
  ( (curl --silent -L "https://github.com/jingweno/gaffer/releases/download/0.0.1/${GAFFER_VERSION}-dist.zip" > tmp/gaffer.zip) &&
    (unzip -d . -n tmp/gaffer.zip) && (mv ${GAFFER_VERSION}/bin/* ./bin) && (mv ${GAFFER_VERSION}/lib/* ./lib) && (rm -Rf ${GAFFER_VERSION}) )
fi
./bin/lein deps
mkdir -p tmp/db/data
ps xau | grep livereload | grep -v grep | awk '{print $2}' | xargs kill
APP_NAME=skeleton
if [ ! -f ./.env ]; then
  printf "DATABASE_URL=mongodb://localhost/${APP_NAME}\n" > ./.env
fi
./bin/gaffer start -f Procfile.dev
