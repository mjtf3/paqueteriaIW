# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-23 AS build

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime image
FROM eclipse-temurin:23-jre

WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Environment variables (can be overridden at runtime)
ENV PORT=8080
ENV PGHOST=localhost
ENV PGPORT=5432
ENV PGDATABASE=paqueteria
ENV PGUSER=postgres
ENV PGPASSWORD=postgres

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
