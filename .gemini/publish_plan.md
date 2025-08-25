# Publishing and Deployment Plan for Ktor Scrap

This document outlines the steps to automatically build, publish, and deploy the Ktor Scrap application using GitHub Actions and Docker Compose for CasaOS.

---

## Part 1: Automated Docker Image Publishing with GitHub Actions

Your project already contains a GitHub Actions workflow at `.github/workflows/docker-publish.yml`. This workflow is well-configured to build and publish a Docker image to the GitHub Container Registry (GHCR) every time you push to the `main` branch.

### How It Works:
1.  **Trigger**: The action runs automatically on a `git push` to the `main` branch.
2.  **Login**: It securely logs into GHCR using a temporary `GITHUB_TOKEN`.
3.  **Build & Push**: It uses the `Dockerfile` in your project root to build the image and then pushes it to GHCR.
4.  **Image Tag**: The image will be tagged as `ghcr.io/your-github-username/your-repo-name:latest`. For example: `ghcr.io/onelenyk/ktor-scrap:latest`.

### Your Action Items:

1.  **Enable GitHub Actions**: Ensure that GitHub Actions are enabled for your repository. Go to `Settings > Actions > General` and make sure "Allow all actions and reusable workflows" is selected.

2.  **Make the Image Public (Required for CasaOS)**: By default, images pushed to GHCR are private. For CasaOS to pull the image without credentials, you must make it public.
    *   Go to your GitHub profile page.
    *   Click on the "Packages" tab.
    *   Find your `ktor-scrap` package.
    *   Go into the package settings and change the visibility to "Public".
    *   **Note**: You only need to do this once per package.

3.  **GCP Service Account Key**: Your `Dockerfile` has this command:
    ```sh
    CMD ["echo ""$GCP_SA_KEY"" > /app/service-account-key.json && java -jar app.jar"]
    ```
    This requires a `GCP_SA_KEY` environment variable containing the entire JSON content of your Google Cloud service account key. This is a sensitive value and should be managed carefully. You will add this secret to CasaOS, not here.

---

## Part 2: Deploying to CasaOS with Docker Compose

Here is a refined `docker-compose.yml` file tailored for a seamless installation on CasaOS.

### `docker-compose.yml` for CasaOS

```yaml
version: '3.8'

# CasaOS Deployment for Ktor Scrap
# 1. In CasaOS, go to the App Store -> Custom Install.
# 2. Paste the entire content of this file into the editor.
# 3. IMPORTANT: Change the 'image' line to match your public GHCR image path.
# 4. Fill in the required environment variables in the CasaOS UI.
# 5. Click "Install".

services:
  ktor-scrap:
    # IMPORTANT: Replace with your actual public GHCR image path.
    # Example: ghcr.io/onelenyk/ktor-scrap:latest
    image: ghcr.io/YOUR_GITHUB_USERNAME/YOUR_REPO_NAME:latest
    container_name: ktor-scrap
    restart: unless-stopped
    ports:
      # CasaOS will manage the host port mapping.
      # This exposes port 8080 from the container.
      - "8080:8080"
    environment:
      # The port the Ktor application will run on inside the container.
      - PORT=8080
      # --- These variables will be configured in the CasaOS UI ---
      - FIRESTORE_PROJECT_ID=your-firestore-project-id
      - CONCURRENT_JOB_LIMIT=10
      # This is a placeholder; you will paste the actual key in the CasaOS UI.
      - SERVICE_ACCOUNT_KEY=paste-your-base64-encoded-service-account-key-json-here

# CasaOS App Store Configuration
x-casaos:
  architectures:
    - amd64
    - arm64
  main: ktor-scrap
  description:
    en_us: "A web scraper application built with Ktor that can be configured to scrape various job boards and websites."
  tagline:
    en_us: "Your Web Scraping Companion"
  developer: "onelenyk"
  author: "onelenyk"
  icon: "https://avatars.githubusercontent.com/u/1099233?v=4" # Using your GitHub avatar as an icon
  thumbnail: ""
  title:
    en_us: "Ktor Scrap"
  port_map: "8080"
  scheme: http
  # This section creates a user-friendly setup form in the CasaOS UI.
  envs:
    - name: "FIRESTORE_PROJECT_ID"
      label:
        en_us: "Firestore Project ID"
      type: "text"
      default: "your-project-id"
      description:
        en_us: "Enter your Google Cloud Firestore Project ID."
    - name: "CONCURRENT_JOB_LIMIT"
      label:
        en_us: "Concurrent Job Limit"
      type: "number"
      default: "10"
      description:
        en_us: "Max number of concurrent scraping jobs."
    - name: "GCP_SA_KEY"
      label:
        en_us: "GCP Service Account Key (JSON)"
      type: "textarea"
      default: ""
      description:
        en_us: "Paste the entire content of your service-account-key.json file here. This is required for Firestore access."

```

### Your Action Items:

1.  **Push to `main`**: Commit and push your latest code to the `main` branch. This will trigger the GitHub Action to build and publish your Docker image. Verify that the action completes successfully in the "Actions" tab of your repository.

2.  **Copy the Compose File**: Open the `docker-compose.yml` file from your project (or the one above).

3.  **Install on CasaOS**:
    *   Navigate to your CasaOS dashboard.
    *   Go to the **App Store**.
    *   Click on **Custom Install** at the top.
    *   Paste the entire YAML content into the editor.
    *   **Crucially**, update the `image:` line to point to your public GHCR image (e.g., `image: ghcr.io/onelenyk/ktor-scrap:latest`).
    *   Click **Submit**.

4.  **Configure in CasaOS UI**: CasaOS will now show you the setup form we defined in the `x-casaos` section.
    *   Enter your `Firestore Project ID`.
    *   Set the `Concurrent Job Limit`.
    *   Carefully paste the **entire JSON content** of your `service-account-key.json` file into the `GCP Service Account Key (JSON)` text area.
    *   Click **Install**.

Your Ktor Scrap application should now be running on CasaOS. You can view its logs and access it through the CasaOS dashboard.
