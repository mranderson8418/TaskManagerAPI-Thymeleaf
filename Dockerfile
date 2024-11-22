

# Use a lightweight base image with JDK 17
FROM openjdk:17-oracle-alpine

ARG JAR_FILE=target/*.jar

# Set the working directory
WORKDIR /app

# Copy the JAR file into the working directory
COPY ./target/TaskManagerAPI-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your application will listen on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "/app.jar"]