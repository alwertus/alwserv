#!/bin/bash
path="$HOME/apps/server/alwserv"

start-stop-daemon -Kvp $path/pid
echo "BACK server stopped"