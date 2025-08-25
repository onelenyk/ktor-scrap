#!/bin/bash

# ==============================================================================
# Docker Build Script for Ktor Scraper
# ==============================================================================
#
# This script builds the Docker image for the application and tags it
# appropriately for the GitHub Container Registry (GHCR).
#
# Usage:
# 1. Make this script executable:
#    chmod +x build-docker.sh
#
# 2. Run the script from the root of the project:
#    ./build-docker.sh
#
# ==============================================================================

# --- Configuration ---
# IMPORTANT: Replace 'your-github-username' with your actual GitHub username.
# For example: GITHUB_USERNAME="onelenyk"
GITHUB_USERNAME="onelenyk"

# The name of your repository.
REPO_NAME="ktor-scrap"

# The tag for the Docker image (e.g., 'latest', 'v1.0.0').
IMAGE_TAG="latest"

# --- Script Logic ---

# Construct the full image name for GHCR.
FULL_IMAGE_NAME="ghcr.io/${GITHUB_USERNAME}/${REPO_NAME}:${IMAGE_TAG}"

# Set color codes for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Starting Docker build...${NC}"
echo "Image will be tagged as: ${GREEN}${FULL_IMAGE_NAME}${NC}"
echo "--------------------------------------------------"

# Run the Docker build command.
# The '.' at the end specifies that the build context is the current directory.
docker build -t "${FULL_IMAGE_NAME}" .

# Check the exit code of the build command.
if [ $? -eq 0 ]; then
    echo "--------------------------------------------------"
    echo -e "${GREEN}Docker build completed successfully!${NC}"
    echo ""
    echo "Your new image is ready: ${FULL_IMAGE_NAME}"
    echo ""
    echo -e "${YELLOW}Next Steps:${NC}"
    echo "1. Log in to GHCR (if you haven't already):"
    echo "   echo YOUR_PAT | docker login ghcr.io -u YOUR_USERNAME --password-stdin"
    echo "   (Replace YOUR_PAT with a GitHub Personal Access Token with 'write:packages' scope)"
    echo ""
    echo "2. Push the image to the registry:"
    echo "   docker push ${FULL_IMAGE_NAME}"
else
    echo "--------------------------------------------------"
    echo -e "\033[0;31mDocker build failed.${NC}"
    echo "Please check the output above for errors."
fi
