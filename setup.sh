#!/usr/bin/env bash
set -e

if [[ -z `which curl` ]]; then
  echo "Please ensure the command line utility curl is installed and try again" && exit 1
fi

if [[ -z `(uname -a | grep Darwin &> /dev/null) && which brew` ]]; then
  echo "Installing brew..."
  /usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
fi

if [[ -z `( (which javac &> /dev/null) && javac -version 2>&1) | grep "1.8"` ]]; then
  echo "Installing java 1.8..."
  ((brew --version > /dev/null) && (brew tap caskroom/cask > /dev/null) && (brew install brew-cask > /dev/null) && (brew cask install java))
  ((apt-get --version > /dev/null) && (apt-get install python-software-properties) && (add-apt-repository ppa:webupd8team/java) && (apt-get update) && (apt-get install -y oracle-java8-installer))
fi

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
