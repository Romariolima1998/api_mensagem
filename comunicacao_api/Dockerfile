# FROM openjdk:21-jdk
FROM maven:3.9.11-eclipse-temurin-11-alpine AS build
WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests

FROM amazoncorretto:11-alpine3.19-jdk


WORKDIR /app

COPY --from=build /app/target/*.jar /app/comunicacao_api.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/comunicacao_api.jar"]