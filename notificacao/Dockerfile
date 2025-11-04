# FROM openjdk:21-jdk
FROM gradle:9.1.0-jdk21-alpine AS build

WORKDIR /app

COPY . .

RUN gradle build --no-daemon

FROM eclipse-temurin:21-jdk-alpine


WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/notificacao.jar

EXPOSE 8081

CMD ["java", "-jar", "/app/notificacao.jar"]