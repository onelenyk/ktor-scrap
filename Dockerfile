# Use OpenJDK 17 runtime image
FROM openjdk:17-jre-slim

# Set working directory
WORKDIR /app

# Copy the pre-built JAR file (Render builds this for you)
COPY build/libs/fat.jar app.jar

# Expose the port (Render expects your app to bind to $PORT)
EXPOSE $PORT

# Run the application
CMD ["java", "-jar", "app.jar"]