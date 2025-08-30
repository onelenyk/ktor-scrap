# ✨ Ktor Scraper

<p align="center">
  <img src="https://www.lenyk.dev/assets/img/portfolio/ktor-scrap.png" alt="Ktor Scraper Dashboard" width="700">
</p>

<p align="center">
    <em>A powerful and elegant web scraping application built with Kotlin and Ktor.</em>
    <br/><br/>
    <a href="https://github.com/onelenyk/ktor-scrap/actions">
        <img src="https://github.com/onelenyk/ktor-scrap/actions/workflows/docker-publish.yml/badge.svg" alt="CI/CD Status">
    </a>
    <a href="https://github.com/onelenyk/ktor-scrap/pkgs/container/ktor-scrap">
        <img src="https://img.shields.io/badge/ghcr.io-onelenyk%2Fktor--scrap-blue?logo=github" alt="GHCR Image">
    </a>
    <a href="https://github.com/onelenyk/ktor-scrap/releases">
        <img src="https://img.shields.io/badge/version-0.0.1-blue" alt="Version">
    </a>
</p>

---

**Ktor Scraper** is designed to scrape job vacancy information from multiple sources with ease. It features a robust asynchronous job processing system, a modern web UI, and a clean, maintainable architecture.

## 🌟 Features

-   **🧩 Multi-Source Scraping**: Easily extendable framework to add new scraping sources.
-   **🚀 Asynchronous Job Processing**: A powerful queueing system manages and executes scraping tasks in the background.
-   **🔥 Firestore Backend**: All data is stored securely in Google Firestore.
-   **🎨 Modern Web UI**: A responsive dashboard built with Bootstrap 5 for easy control and monitoring.
-   **🤖 REST API**: A clean API for programmatic control over scraping jobs.
-   **🏗️ Clean Architecture**: Promotes separation of concerns and maintainability.
-   **💉 Dependency Injection**: Koin is used for managing dependencies.

## 🛠️ Getting Started

Follow these steps to get the application running on your local machine.

### Prerequisites

-   **Java 21** or higher
-   **Docker** and **Docker Compose**
-   A **Google Cloud Platform (GCP)** project with **Firestore** enabled.

### ⚙️ Setup

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/onelenyk/ktor-scrap.git
    cd ktor-scrap
    ```

2.  **Configure Firestore Credentials:**
    -   Go to your GCP project's **IAM & Admin -> Service Accounts**.
    -   Create a new service account with the **"Cloud Datastore User"** role.
    -   Create a JSON key for this service account and download it.

3.  **Set Environment Variables:**
    Create a `.env` file in the root of the project. Copy the contents of your downloaded service account JSON into the `SERVICE_ACCOUNT_KEY` variable.

    ```env
    # .env file
    FIRESTORE_PROJECT_ID=your-gcp-project-id
    SERVICE_ACCOUNT_KEY='{"type": "service_account", "project_id": "...", ...}'
    ```

### 🏃‍♀️ Running the Application

You can run the application locally in two ways:

#### With Docker (Recommended)

This is the easiest way to get started. It uses the pre-built image from the GitHub Container Registry.

1.  **Ensure your `.env` file is configured correctly.**
2.  **Start the application:**
    ```sh
    docker compose up -d
    ```

#### With Gradle

If you want to run the application without Docker:

1.  **Place your service account key** at `src/main/resources/service-account-key.json`.
2.  **Set the `FIRESTORE_PROJECT_ID`** environment variable in your shell.
3.  **Run the application:**
    ```sh
    ./gradlew run
    ```

Once running, the web dashboard will be available at [http://localhost:8080](http://localhost:8080).

---

## 🚢 Publishing

This project uses **GitHub Actions** to automatically build and publish the Docker image. For detailed instructions on the publishing process, versioning, and manual publishing, please see the [**Publishing Guide**](./PUBLISHING.md).

---

## 🕹️ Usage

The web dashboard provides an intuitive interface for managing the scraper.

-   **🔄 Refresh List**: Updates the list of scraping jobs.
-   **➕ Add Custom Job**: Opens a modal to define and start a new scraping job.
-   **🗑️ Remove All Jobs**: Deletes all jobs from the database.
-   **🔍 Actions (per job)**:
    -   **View**: See the scraped vacancies as a formatted list or raw JSON.
    -   **Restart**: Reruns the scraping job.
    -   **Remove**: Deletes the specific job.
