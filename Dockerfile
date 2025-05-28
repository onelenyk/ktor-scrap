# Build stage
FROM gradle:8.2-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle buildFatJar --no-daemon

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/build/libs/fat.jar /app/app.jar

# Create a non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"] 