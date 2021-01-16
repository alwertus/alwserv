#!/bin/bash
srcDir="$HOME/apps/git/alwserv/target"
srcFileName="alwserv.jar"
targetDir="$HOME/apps/server/alwserv"
targetFileName="alwserv.jar"

function update_git {
  cd $srcDir/..
  git pull
}

function create_package {
  mvn clean package
}

function copy_files {
  echo "Copy files..."
  cp -r "$srcDir"/"$srcFileName" "$targetDir"/"$targetFileName"
  cp -r start.sh "$targetDir"/start.sh
  cp -r stop.sh "$targetDir"/stop.sh
  echo "Complete"
}

./stop.sh && update_git && create_package && copy_files && ./start.sh