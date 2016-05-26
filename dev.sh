#!/usr/bin/env bash
set -e

MY_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

cd ${MY_DIR}

${MY_DIR}/setup.sh

(mongod --version &> /dev/null) || ( (brew --version &> /dev/null) && (echo "Installing MongoDB") && (brew install mongodb > /dev/null) )
(mongod --version &> /dev/null) || ( (apt-get --version &> /dev/null)&& (echo "Installing MongoDB") && (apt-get install -y mongodb-org > /dev/null) )

mkdir -p ${MY_DIR}/tmp/db/data
ps xau | grep livereload | grep -v grep | awk '{print $2}' | xargs kill

APP_NAME='skeleton'
if [[ ! -f ${MY_DIR}/.env ]]; then
  echo "Setting up initial .env"
  printf "DATABASE_URL=mongodb://localhost/${APP_NAME}\nDEV=true\n" > ${MY_DIR}/.env
fi

echo "Starting servers..."
${MY_DIR}/bin/lein cooper
