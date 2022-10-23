#!/bin/bash

docker-compose -f docker-compose-prod.yml down

git pull origin master

docker-compose -f docker-compose-prod.yml build

docker-compose -f docker-compose-prod.yml up -d

docker-compose -f docker-compose-prod.yml logs -f

exit 0
