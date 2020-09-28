FROM openjdk:11 as builder

WORKDIR /quarkus

COPY .mvn/ ./.mvn/
COPY mvnw ./
COPY pom.xml ./

RUN ./mvnw dependency:resolve dependency:resolve-plugins

COPY src/ src/
RUN ./mvnw package -DskipTests

FROM gcr.io/distroless/java:11

COPY --from=amd64/busybox:1.31.1 /bin/busybox /busybox/busybox
RUN ["/busybox/busybox", "--install", "/bin"]

WORKDIR /quarkus

EXPOSE 8080 9090

ENV QUARKUS_DATASOURCE_USERNAME=quarkus QUARKUS_DATASOURCE_PASSWORD=1qay2wsx
ENV QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://database:5432/weather

ENTRYPOINT ["java", "-server", "-Xshare:auto", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-XX:ThreadStackSize=256", "-XX:MaxMetaspaceSize=128m", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=250", "-XX:+UseStringDeduplication", "-Dquarkus.http.host=0.0.0.0", "-Djava.util.logging.manager=org.jboss.logmanager.LogManager", "-Djava.security.egd=file:/dev/./urandom", "-Dcom.sun.management.jmxremote", "-Dcom.sun.management.jmxremote.port=9090", "-Djava.rmi.server.hostname=localhost", "-Dcom.sun.management.jmxremote.rmi.port=9090", "-Dcom.sun.management.jmxremote.ssl=false", "-Dcom.sun.management.jmxremote.authenticate=false", "-jar", "ldap-search-service-1.0.0-runner.jar"]

COPY --from=builder /quarkus/target/lib/ ./lib/
COPY --from=builder /quarkus/target/ldap-search-service-1.0.0-runner.jar ./
COPY --from=builder /quarkus/target/ldap-search-service-1.0.0.jar ./
