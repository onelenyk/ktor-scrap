# Build stage
FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY . .
# Using 'shadowJar' as it's a common and robust way to create fat jars.
# If 'buildFatJar' is a custom task, it can be used instead.
RUN gradle buildFatJar --no-daemon

# Run stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create a non-root user and group
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy the application JAR from the build stage
COPY --from=build /app/build/libs/fat.jar app.jar

# Change ownership of the application files to the non-root user
RUN chown -R appuser:appgroup /app


# Set the user to the non-root user
USER appuser

# Set default port
ENV PORT=8080
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=5s --start-period=15s --retries=3 \
  CMD wget -q -O - http://localhost:${PORT}/live || exit 1

# Add OCI labels for better metadata
LABEL org.opencontainers.image.title="Ktor Scrap" \
      org.opencontainers.image.description="A web scraper application built with Ktor." \
      org.opencontainers.image.source="https://github.com/onelenyk/ktor-scrap" \
      org.opencontainers.image.licenses="MIT"

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
