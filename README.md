# LDAP Search Service

Demo service that combines a Quarkus REST service with Apache DS as search backend.

## Prerequisites

- Zulu JDK 11.0.6 or higher
- GraalVM CE 20.1.0 installed and configured
- Apache Maven 3.6.3 or higher

## Build and Run locally

```bash
$ docker-compose up -d database

$ ./mvnw compile quarkus:dev
$ ./mvnw clean install

$ cd target/
$ java -server -Xmx512m -Xss256k -Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager -jar ldap-search-service-1.0.0-runner.jar

$ export GRAALVM_HOME=$JAVA_HOME
$ ${GRAALVM_HOME}/bin/gu install native-image

$ ./mvnw package -Pnative
$ ./target/ldap-search-service-1.0.0-runner
```

## Build and Run with Docker

```bash
$ docker build -t ldap-search-service:1.0.0 .
$ docker build -t ldap-search-service:1.0.0 -f Dockerfile.native .
$ docker build -t ldap-search-service:1.0.0 -f Dockerfile.graalvm .
$ docker build -t ldap-search-service:1.0.0 -f Dockerfile.zulu .

$ docker-compose --compatibility up
```

## Exercise the Microservice

```bash
$ http get localhost:8080/api/search dn=="ou=people,o=sevenSeas" filter=="(&(objectClass=person))"
$ http get localhost:8080/api/search dn=="cn=John Fryer,ou=people,o=sevenSeas" filter=="(&(objectClass=person))"

$ curl -X GET http://localhost:8080/
$ curl -X GET http://localhost:8080/metrics
$ curl -X GET http://localhost:8080/health
$ curl -X GET http://localhost:8080/health/ready
$ curl -X GET http://localhost:8080/health/live
```

## Load Testing

Assuming you have started InfluxDB and Grafana using Docker Compose from the `src/test/k6` of this repository, 
you can now start the K6 load test for the weather service.

```bash
$ cd src/test/k6/
$ docker-compose up

$ k6 run -u 50 -d 60s ldap-search.js -o influxdb=http://localhost:8086/k6
$ k6 run -u 100 -d 120s ldap-search.js -o influxdb=http://localhost:8086/k6
$ k6 run -u 0 -s 10s:100 -s 100s -s 10s:0 ldap-search.js -o influxdb=http://localhost:8086/k6
```

# Maintainer

M.-Leander Reimer (@lreimer), <mario-leander.reimer@qaware.de>

# License

This software is provided under the MIT License, read the `LICENSE` file for details.
