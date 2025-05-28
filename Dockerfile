# Build stage
FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle buildFatJar --no-daemon

# Run stage
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/fat.jar app.jar

# Expose the port (Render expects your app to bind to $PORT)
EXPOSE $PORT

# Run the application
CMD ["java", "-jar", "app.jar"]