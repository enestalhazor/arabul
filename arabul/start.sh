#!/bin/bash

service postgresql start
service postgresql status

# Start static server here
serve -s /productphotos -l 0.0.0.0:8090 &

echo "starting application"
java -jar /home/app.jar