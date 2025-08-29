# 🚀 Publishing Guide

This guide provides detailed instructions for building, tagging, and publishing the Ktor Scraper Docker image to the GitHub Container Registry (GHCR).

## Table of Contents

- [Automated Publishing (CI/CD)](#automated-publishing-cicd)
- [Manual Publishing](#manual-publishing)
  - [Prerequisites](#prerequisites)
  - [Step 1: Build the Docker Image](#step-1-build-the-docker-image)
  - [Step 2: Log in to GHCR](#step-2-log-in-to-ghcr)
  - [Step 3: Push the Image](#step-3-push-the-image)
- [Versioning Strategy](#versioning-strategy)

---

## Automated Publishing (CI/CD)

The recommended and most reliable way to publish the application is through the automated CI/CD pipeline.

The workflow is defined in `.github/workflows/docker-publish.yml` and is triggered automatically on every push to the `master` branch.

### Workflow Steps:
1.  **Checkout Code**: The workflow checks out the latest code from the `master` branch.
2.  **Build Image**: It builds the Docker image using the `Dockerfile` in the root of the project.
3.  **Login to GHCR**: It securely logs into the GitHub Container Registry using a temporary `GITHUB_TOKEN`.
4.  **Push Image**: It pushes the newly built image and tags it as `ghcr.io/onelenyk/ktor-scrap:latest`.

> **Note:** No manual intervention is required for this process. Simply push your changes to `master`, and the new image will be published automatically.

---

## Manual Publishing

There may be times when you need to build and publish an image manually (e.g., for testing a feature branch).

### Prerequisites

-   **Docker**: Ensure Docker is installed and running on your local machine.
-   **GitHub Personal Access Token (PAT)**: You need a PAT with the `write:packages` scope to push images to GHCR. You can create one [here](https://github.com/settings/tokens).

### Step 1: Build the Docker Image

Use the provided helper script to build the image. This script ensures the image is tagged with the correct name for GHCR.

```sh
./dot/build-docker.sh
```

This will create an image named `ghcr.io/onelenyk/ktor-scrap:latest`.

### Step 2: Log in to GHCR

Log in to the GitHub Container Registry using your GitHub username and the PAT you created.

```sh
echo YOUR_PAT | docker login ghcr.io -u YOUR_USERNAME --password-stdin
```
*Replace `YOUR_PAT` and `YOUR_USERNAME` with your credentials.*

### Step 3: Push the Image

Push the locally built image to GHCR.

```sh
docker push ghcr.io/onelenyk/ktor-scrap:latest
```

---

## Versioning Strategy

While the automated pipeline uses the `:latest` tag for simplicity, it's a good practice to use semantic versioning for more stable releases.

### Recommended Tagging Scheme:

-   `ghcr.io/onelenyk/ktor-scrap:latest`: Always points to the most recent build from the `master` branch.
-   `ghcr.io/onelenyk/ktor-scrap:v1.2.3`: Points to a specific, stable release.

To push a versioned tag, you can modify the `IMAGE_TAG` in `dot/build-docker.sh` before running it, or manually tag your image:

```sh
# Manually tag the latest build with a version
docker tag ghcr.io/onelenyk/ktor-scrap:latest ghcr.io/onelenyk/ktor-scrap:v1.0.0

# Push the new tag
docker push ghcr.io/onelenyk/ktor-scrap:v1.0.0
```
