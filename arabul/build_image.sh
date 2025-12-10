#!/bin/bash

echo "Building image... " && mvn clean install && docker build -t arabul -f Dockerfile . && echo "Image built success"
