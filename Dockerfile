# Use the official OpenJDK 17 image as a parent image
FROM openjdk:17

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file built by Maven into the container
COPY target/quickstart-0.0.1.jar quickstart.jar

# Expose the port your Spring Boot application runs on (if needed)
 EXPOSE 9193

# Command to run the application
CMD ["java", "-jar", "quickstart.jar"]




