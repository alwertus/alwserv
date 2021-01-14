#!/bin/bash
srcDir="$HOME/apps/git/alwserv/target"
srcFileName="alwserv.jar"
targetDir="$HOME/apps/server/alwserv"
targetFileName="alwserv.jar"
properties="$targetDir"/application.properties

function create_package {
  mvn clean package
}

function copy_files {
  mkdir -p "$targetDir"
  cp -r "$srcDir"/"$srcFileName" "$targetDir"/"$targetFileName"

cat <<EOT >> "$properties"
server.port=<CHANGE_ME>

spring.datasource.url=jdbc:mysql://192.168.1.8:3306/<CHANGE_ME>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=<CHANGE_ME>
spring.datasource.password=<CHANGE_ME>

application.jwt.secretKey=<CHANGE_ME_LOOOOOOOOOOOOONG>
application.jwt.tokenPrefix=Bearer
application.jwt.tokenExpirationAfterDays=14
EOT

cat <<EOT >> "$properties"
count=1

app1.title=Double Commander
app1.find=dmS dcmd doublecmd
app1.start=zzzSHzzzscreen -dmS dcmd doublecmd
app1.stop=zzzKILLzzz
EOT

}

create_package && copy_files