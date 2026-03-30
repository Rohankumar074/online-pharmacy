FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /workspace
COPY . .
RUN mvn -f catalog-service/pom.xml -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/catalog-service/target/catalog-service-*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]

