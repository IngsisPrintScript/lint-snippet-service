# syntax=docker/dockerfile:1.4

# --- Stage 1: build the application ---
FROM gradle:8.4-jdk21-alpine AS builder

WORKDIR /home/gradle/project

# Copiar sólo archivos necesarios para cachear dependencias
COPY build.gradle settings.gradle gradlew gradle /home/gradle/project/

# Descargar dependencias usando tu ~/.gradle del HOST
RUN --mount=type=bind,source=/home/MackyAlimena/.gradle,target=/home/gradle/.gradle \
    gradle --no-daemon build -x test || return 0

# Copiar el resto del código
COPY . /home/gradle/project

# Build real, generando el .jar (otra vez montando tus credenciales)
RUN --mount=type=bind,source=/home/MackyAlimena/.gradle,target=/home/gradle/.gradle \
    gradle --no-daemon clean bootJar

# --- Stage 2: run the application ---
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar el jar generado desde el stage anterior
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar

ENV JAVA_OPTS=""

EXPOSE 8081

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
