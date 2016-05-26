#!/usr/bin/env bash
set -e

MY_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

cd ${MY_DIR}

mkdir -p ${MY_DIR}/bin
mkdir -p ${MY_DIR}/tmp
mkdir -p ${MY_DIR}/lib
if [[ ! -f ${MY_DIR}/bin/lein ]]; then
  echo "Installing local lein..."
  (curl --version > /dev/null) || ((brew --version > /dev/null) && (brew install curl > /dev/null))
  (curl --version > /dev/null) || ((apt-get --version > /dev/null) && (apt-get install curl -y > /dev/null))
  curl --silent "https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein" > ${MY_DIR}/bin/lein
fi
chmod +x ${MY_DIR}/bin/lein
${MY_DIR}/bin/lein &> /dev/null
echo "Installing dependencies..."
${MY_DIR}/bin/lein deps &> /dev/null
