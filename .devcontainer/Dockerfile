# Use the official Maven image as the parent image
FROM maven:3.8.3-openjdk-17-slim AS build

# Set the working directory to /app
WORKDIR /app

# Copy the pom.xml and package.json files to the container
COPY pom.xml .
COPY frontend/package.json ./frontend/

# Install the frontend dependencies
RUN cd frontend && npm install

# Copy the rest of the application code to the container
COPY . .

# Build the application
RUN mvn clean package

# Use the official OpenJDK image as the parent image
FROM openjdk:17-slim

# Set the working directory to /app
WORKDIR /app

# Copy the built JAR file from the previous stage to the container
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080 for the Spring Boot application
EXPOSE 8080

# Start the Spring Boot application when the container starts
CMD ["java", "-jar", "app.jar"]