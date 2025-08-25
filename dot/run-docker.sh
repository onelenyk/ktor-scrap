#!/bin/bash

# This script runs the Ktor Scrap Docker image with necessary environment variables.
# Remember to replace the placeholder values with your actual configuration.

# --- Configuration --- 
# The port your Ktor application will run on inside the container.
# This should match the EXPOSE port in your Dockerfile and the port mapping below.
export PORT=8080

# Your Google Cloud Firestore Project ID.
# Example: my-ktor-scrap-project-12345
export FIRESTORE_PROJECT_ID="backend-11d6"

# The maximum number of concurrent scraping jobs.
export CONCURRENT_JOB_LIMIT=10

# Your Base64 encoded Google Cloud service account key JSON.
# You can generate this using the EncodeServiceAccountKeyKt utility:
# java -jar your-app.jar /path/to/service-account-key.json
export SERVICE_ACCOUNT_KEY="service"

# --- Docker Run Command ---
docker run \
  -p ${PORT}:${PORT} \
  -e PORT=${PORT} \
  -e FIRESTORE_PROJECT_ID="${FIRESTORE_PROJECT_ID}" \
  -e CONCURRENT_JOB_LIMIT=${CONCURRENT_JOB_LIMIT} \
  -e SERVICE_ACCOUNT_KEY="${SERVICE_ACCOUNT_KEY}" \
  ghcr.io/onelenyk/ktor-scrap:latest
