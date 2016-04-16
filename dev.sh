#!/usr/bin/env bash
set -e

./setup.sh
(mongod --version &> /dev/null) || ( (brew --version &> /dev/null) && (brew install mongodb > /dev/null) )
(mongod --version &> /dev/null) || ( (apt-get --version &> /dev/null) && (apt-get install -y mongodb-org > /dev/null) )

PROVIDER='hugocorbucci' # Original provider: 'jingweno'
GAFFER_VERSION='0.0.2'
if [[ ! -f ./bin/gaffer || ${GAFFER_VERSION} != `./bin/gaffer version` ]]; then
  ( (curl --silent -L "https://github.com/${PROVIDER}/gaffer/releases/download/${GAFFER_VERSION}/gaffer-${GAFFER_VERSION}-dist.zip" > tmp/gaffer.zip) &&
    (unzip -d . -n tmp/gaffer.zip) && (mv gaffer-${GAFFER_VERSION}/bin/* ./bin) && (mv gaffer-${GAFFER_VERSION}/lib/* ./lib) && (rm -Rf gaffer-${GAFFER_VERSION}) )
fi

./bin/lein deps

mkdir -p tmp/db/data
ps xau | grep livereload | grep -v grep | awk '{print $2}' | xargs kill

APP_NAME='skeleton'
if [ ! -f ./.env ]; then
  printf "DATABASE_URL=mongodb://localhost/${APP_NAME}\n" > ./.env
fi
./bin/gaffer start -f Procfile.dev
