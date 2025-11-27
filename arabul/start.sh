#!/bin/bash

service postgresql start
service postgresql status

# Start static server here
serve /productphotos -p 8090 &

echo "starting application"
java -jar /home/app.jar