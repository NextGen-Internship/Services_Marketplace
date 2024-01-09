# Stage 1: Build the application
FROM maven:3.9.5-openjdk-11 AS builder

WORKDIR /app

COPY . .

RUN mvn clean package

# Stage 2: Create the final image
FROM openjdk:17

WORKDIR /app

COPY --from=builder /app/target/marketplace-0.0.1-SNAPSHOT.jar /app/marketplace-0.0.1-SNAPSHOT.jar

# Specify the command to run on container startup
CMD ["java", "-jar", "marketplace-0.0.1-SNAPSHOT.jar"]
