# iObeya-Categories POC

Proof of Concept iObeya Categories

## Prerequisites

- Internet connection (to get docker images + maven deps + JS deps)
- docker & docker compose (tested on linux)
- OpenJDK 11 (to compile locally)

## How to run

- build docker image : `docker compose build`
- Start stack : `docker compose up` or `docker compose up -d`

## Run in dev mode

If you want to run the application from an ide (ex: eclipse), you can :

- Disable **iobeya-categories-poc-app** from **docker-compose.yml**
- Start the stack : `docker compose up` or `docker compose up -d`
- Import the project as a maven project
- Customize properties in **src/main/resources/application.properties**
- Start as a Spring boot application

## Populate database

Schema is created automatically on mysql startup : **iobeya-categories-init.sql** is executed because it is copied under **/docker-entrypoint-initdb.d**.

From phpmyadmin or adminer, execute **iobeya-categories-fill.sql** :

```sql
TRUNCATE TABLE `iobeya`.`categories`;

CALL generate1000categories();

CALL generateChildCategories();

```

This script will randomly generate data.