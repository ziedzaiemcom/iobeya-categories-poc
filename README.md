# iObeya-Categories POC

Proof of Concept iObeya Categories

Links :
- Github : https://github.com/ziedzaiemcom/iobeya-categories-poc
- PROD : https://iobeya-categories-poc-app.ziedzaiem.com
- PhpMyAdmin : https://iobeya-categories-poc-phpmyadmin.ziedzaiem.com/  (iobeya/iobeya)

## Prerequisites

- Internet connection (to get docker images + maven deps + JS deps)
- docker & docker compose (tested on linux)
- OpenJDK >= 11 (to compile locally)
- IDE with [Lombok](https://projectlombok.org/) configured 

## How to run

### fast

- build docker image : `docker compose build`
- Start stack : `docker compose up` or `docker compose up -d`

### Run in dev mode

If you want to run the application from an ide (ex: eclipse), you can :

- Disable **iobeya-categories-poc-app** from **docker-compose.yml**
- Start the stack : `docker compose up` or `docker compose up -d`
- Import the project as a maven project
- Customize properties in **src/main/resources/application.properties**
- Launch as a Spring boot application

If you have trouble with Kafka, you can set `light.mode=true` in **src/main/resources/application.properties** to use internal broker.

### Run without docker

- Setup a mysql/mariadb database
- Use **iobeya-categories-init.sql** to create the schema and the procedures.
- Customize properties in **src/main/resources/application.properties**, set `light.mode=true`
- Start the application as a maven project

### Run without ide

- Setup a mysql/mariadb database
- Use **iobeya-categories-init.sql** to create the schema and the procedures.
- Customize properties in **src/main/resources/application.properties**, set `light.mode=true`
- start with maven and command line : `mvn clean install -Dmaven.test.skip=true && java -jar target/poc-0.0.1-SNAPSHOT.jar`

## Populate database

Schema is created automatically on mysql startup : **iobeya-categories-init.sql** is executed because it is copied under **/docker-entrypoint-initdb.d**.

From phpmyadmin or adminer, execute **iobeya-categories-fill.sql** :

```sql
TRUNCATE TABLE `iobeya`.`categories`;

CALL generate1000categories();

CALL generateChildCategories();

```

This script will randomly generate data, it can load more than 2 millions records. Can take some time to execute depending on your CPU.

- **generate1000categories** : generate 1000 root categories
- **generateChildCategories** : randomly generate children from depth 1 to 10.

## 🪦 A moment of silence for our fallen soldier...

```
$ uptime
20:43:06 up 602 days,  8:34,  2 users,  load average: 116.43, 177.95, 126.05
$ uptime -s
2021-02-28 12:08:29
```

You REALLY need a fast machine with at least 4go RAM to run this poc.
