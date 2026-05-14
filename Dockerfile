# syntax=docker/dockerfile:1

# --- Build stage ---
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Cache Maven dependencies
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# Copy sources and build
COPY src ./src
RUN mvn -q -DskipTests package

# --- Runtime stage ---
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# Make healthchecks reliable (curl is needed by docker-compose healthcheck)
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*

# Copy fat jar
# Spring Boot plugin default output: target/*.jar
COPY --from=builder /app/target/*.jar app.jar

# Spring Boot listens on 8080 by default
EXPOSE 8080

# Defaults (can be overridden by env vars at runtime)
ENV SERVER_PORT=8080 \
    SPRING_PROFILES_ACTIVE=prod \
    SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/postgres \
    SPRING_DATASOURCE_USERNAME=postgres \
    SPRING_DATASOURCE_PASSWORD=postgres \
    SPRING_JPA_HIBERNATE_DDL_AUTO=validate

# Entrypoint: use env vars in standard Spring Boot names
# (Spring automatically maps env vars to properties)
ENTRYPOINT ["sh","-c","java -jar /app/app.jar --server.port=${SERVER_PORT} --spring.profiles.active=${SPRING_PROFILES_ACTIVE} --spring.datasource.url=${SPRING_DATASOURCE_URL} --spring.datasource.username=${SPRING_DATASOURCE_USERNAME} --spring.datasource.password=${SPRING_DATASOURCE_PASSWORD} --spring.jpa.hibernate.ddl-auto=${SPRING_JPA_HIBERNATE_DDL_AUTO}"]
