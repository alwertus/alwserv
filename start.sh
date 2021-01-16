#!/bin/bash
path="$HOME/apps/server/alwserv"
jarName="alwserv.jar"

java="/usr/bin/java"

start-stop-daemon -Sbvmp $path/pid -d $path/ -x $java -- -jar $path/$jarName

pid=$( cat $path/pid)
echo "Server is started. PID=$pid"