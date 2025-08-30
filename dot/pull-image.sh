#!/bin/bash

# ==============================================================================
# Docker Pull Script for Ktor Scraper
# ==============================================================================
#
# This script pulls the latest version of the application's Docker image
# from the GitHub Container Registry (GHCR).
#
# Usage:
# 1. Make this script executable:
#    chmod +x pull-image.sh
#
# 2. Run the script from the root of the project:
#    ./dot/pull-image.sh
#
# ==============================================================================

# --- Configuration ---
# The full name of the image to pull from GHCR.
FULL_IMAGE_NAME="ghcr.io/onelenyk/ktor-scrap:latest"

# --- Script Logic ---

# Set color codes for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Attempting to pull the latest Docker image...${NC}"
echo "Image: ${GREEN}${FULL_IMAGE_NAME}${NC}"
echo "--------------------------------------------------"

# Run the Docker pull command.
docker pull "${FULL_IMAGE_NAME}"

# Check the exit code of the pull command.
if [ $? -eq 0 ]; then
    echo "--------------------------------------------------"
    echo -e "${GREEN}Docker image pulled successfully!${NC}"
    echo "You can now run the updated container using 'docker compose up -d'."
else
    echo "--------------------------------------------------"
    echo -e "\033[0;31mDocker pull failed.${NC}"
    echo "Please check the output above for errors. Ensure you are logged in to GHCR."
fi
