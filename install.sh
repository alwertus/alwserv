#!/bin/bash
srcDir="$HOME/apps/git/alwserv/target"
srcFileName="alwserv.jar"
targetDir="$HOME/apps/server/alwserv"
targetFileName="alwserv.jar"
programsCfg="$targetDir"/programs.cfg
properties="$targetDir"/application.properties

function create_package {
 mvn clean package
}

function copy_files {
  mkdir -p "$targetDir"
  cp -r "$srcDir"/"$srcFileName" "$targetDir"/"$targetFileName"
  cp -r start.sh "$targetDir"/start.sh
  cp -r stop.sh "$targetDir"/stop.sh
}

function create_configs {

cat <<EOT >> "$properties"
server.port=<CHANGE_ME>

spring.datasource.url=jdbc:mysql://192.168.1.8:3306/<CHANGE_ME>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=<CHANGE_ME>
spring.datasource.password=<CHANGE_ME>

application.jwt.secretKey=<CHANGE_ME_LOOOOOOOOOOOOONG>
application.jwt.tokenPrefix=Bearer
application.jwt.tokenExpirationAfterDays=14

application.allowed.origins=<CHANGE_ME>
application.allowed.methods=GET,POST
application.allowed.headers=Authorization,Cache-Control,Content-Type
EOT

cat <<EOT >> "$programsCfg"
count=1

app1.title=Double Commander
app1.find=dmS dcmd doublecmd
app1.start=zzzSHzzzscreen -dmS dcmd doublecmd
app1.stop=zzzKILLzzz
EOT
}

function echo_result {
  echo "WARNING!!! You must correct config files:"
  echo "$properties"
  echo "$programsCfg"
}

create_package && copy_files && echo_result && create_configs