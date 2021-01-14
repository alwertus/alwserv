#!/bin/bash
#set -e
pathServer="$HOME/apps/server/alwserv"
jarName="alwserv.jar"
java="/usr/bin/java"

cd $pathServer
start-stop-daemon -Sb -d $pathServer/ -p $pathServer/pid -x $java -- -jar $pathServer/$jarName
echo "Server is started"