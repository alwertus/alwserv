#!/bin/bash
#set -e
pathServer="$HOME/apps/server/alwserv"
targetFileName="alwserv.jar"
java="/usr/bin/java"

echo "Stopping server"
result=$(start-stop-daemon -Kvx $java -- -jar $targetDir/$targetFileName)
if [[ "$result" =~ .*"Stopped".* ]];
then
  echo "Success"
else
  echo "Server was not started"
fi