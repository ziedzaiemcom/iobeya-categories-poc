FROM maven:3.8.6-eclipse-temurin-11 as builder

RUN mkdir /app

ADD . /app

WORKDIR /app

RUN mvn clean install -Dmaven.test.skip=true

FROM eclipse-temurin:11

COPY --from=builder /app/target/poc-0.0.1-SNAPSHOT.jar /poc-0.0.1-SNAPSHOT.jar

CMD ["/bin/bash", "-c", "sleep 10;java -jar /poc-0.0.1-SNAPSHOT.jar"]