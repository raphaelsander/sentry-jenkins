#!/bin/bash

docker compose down
docker volume rm jenkins_jenkins_home
docker compose up --build