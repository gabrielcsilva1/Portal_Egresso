FROM eclipse-temurin:23-alpine-3.21 AS build

WORKDIR /src/app

COPY pom.xml mvnw mvnw.cmd ./

COPY .mvn ./.mvn

COPY src ./src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:23-alpine-3.21

WORKDIR /src/app

COPY --from=build ./src/app/target/portal_egresso_api.jar ./portal_egresso_api.jar

EXPOSE 8080

CMD ["java", "-jar", "portal_egresso_api.jar"]