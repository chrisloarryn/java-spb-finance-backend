# Build stage
FROM maven:3-eclipse-temurin-25-alpine AS build
WORKDIR /app

COPY src ./src

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN mvn clean package -B

# log the output of the build
RUN ls -la /app/target

# Runtime stage
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar cp.jar

# Exponer el puerto 8080
EXPOSE 8080
RUN ls -la /app

# Run the application
ENTRYPOINT ["java", "-jar", "cp.jar"]
