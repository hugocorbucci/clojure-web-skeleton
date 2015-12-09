#!/usr/bin/env bash
set -e
set -o

./setup.sh
(brew --version &> /dev/null) && (brew install mongodb > /dev/null)
(apt-get --version &> /dev/null) && (apt-get install -y mongodb-org > /dev/null)
if [ ! -f ./bin/gaffer ]; then
  ((curl --silent -L "https://github.com/jingweno/gaffer/releases/download/0.0.1/gaffer-0.0.1-dist.zip" > tmp/gaffer.zip) &&
    (unzip -d . -n tmp/gaffer.zip))
fi
mkdir -p tmp/db/data
ps xau | grep livereload | grep -v grep | awk '{print $2}' | xargs kill
APP_NAME=skeleton
grep "DATABASE_URL" ./.env || echo "DATABASE_URL=mongodb://localhost/${APP_NAME}" >> .env
./bin/gaffer start -f Procfile.dev
