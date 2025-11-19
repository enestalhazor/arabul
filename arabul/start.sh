#!/bin/bash

service postgresql start
service postgresql status
echo "starting application"
java -jar /home/app.jar