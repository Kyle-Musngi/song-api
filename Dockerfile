# Build Stage: This downloads Maven and Java 17 to build your app
FROM maven:3.9.4-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Run Stage: This runs the built .jar file
FROM eclipse-temurin:17-jre
COPY --from=build target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]