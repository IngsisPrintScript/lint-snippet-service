# syntax=docker/dockerfile:1

# --- Stage 1: build the application ---
FROM gradle:8.4-jdk21-alpine AS builder

WORKDIR /home/gradle/project

COPY . .

# Construir el .jar (Gradle usará las credenciales de GitHub Actions)
RUN gradle --no-daemon clean bootJar

# --- Stage 2: run the application ---
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar el jar generado desde el stage anterior
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar

ENV JAVA_OPTS=""

EXPOSE 8081

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
