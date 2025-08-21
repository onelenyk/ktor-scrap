# Ktor Scraper

Ktor Scraper is a powerful and flexible web scraping application built with Kotlin and Ktor. It's designed to scrape job vacancy information from multiple sources, manage scraping tasks, and provide a user-friendly web interface for control and monitoring. The project is backed by Google Firestore for data storage and is architected for easy extension and maintenance.

## Features

-   **Multi-Source Scraping:** Easily extendable framework to add new scraping sources.
-   **Asynchronous Job Processing:** A robust queueing system manages and executes scraping tasks in the background.
-   **Firestore Backend:** All data, including job details and results, is stored in Google Firestore.
-   **Modern Web UI:** A responsive dashboard built with Bootstrap 5 allows you to:
    -   View all scraping jobs, grouped by source.
    -   See job statuses (PENDING, PROCESSING, COMPLETED, FAILED) with color-coded highlighting.
    -   View formatted scraping results or the raw JSON.
    -   Create custom scraping jobs.
    -   Restart, or remove jobs.
    -   Remove all jobs at once.
-   **REST API:** A clean API for programmatic control over the scraping jobs.
-   **Clean Architecture:** The codebase is organized into `data`, `domain`, and `presentation` layers, promoting separation of concerns and maintainability.
-   **Dependency Injection:** Koin is used for managing dependencies.

## Architecture

The project follows the principles of Clean Architecture, separating the code into three main layers:

-   **`presentation`:** Handles all UI and API-related logic. This includes the Ktor server setup, routing, and the static web dashboard.
-   **`domain`:** Contains the core business logic and models of the application, such as the `JobQueueManager` and `Scraper` interfaces. It is completely independent of any framework or database.
-   **`data`:** Responsible for data sources. This layer includes the implementation of the repositories and the Firestore integration.

## Getting Started

### Prerequisites

-   Java 11 or higher
-   A Google Cloud Platform (GCP) project with Firestore enabled.

### Setup

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/onelenyk/ktor-scrap.git
    cd ktor-scrap
    ```

2.  **Configure Firestore Credentials:**
    -   Go to your GCP project's IAM & Admin -> Service Accounts.
    -   Create a new service account with the "Cloud Datastore User" role.
    -   Create a key for this service account (JSON format) and download it.
    -   Place the downloaded JSON key file at `src/main/resources/service-account-key.json`.

3.  **Set Environment Variables:**
    The application requires one environment variable to be set:
    -   `FIRESTORE_PROJECT_ID`: Your Google Cloud Platform project ID.

    You can set this in your IDE's run configuration or export it in your shell:
    ```sh
    export FIRESTORE_PROJECT_ID="your-gcp-project-id"
    ```

### Running the Application

You can run the application using the included Gradle wrapper:

```sh
./gradlew run
```

The server will start on port `8080` by default. You can access the web dashboard by navigating to `http://localhost:8080` in your browser.

## Usage

The web dashboard provides a simple and intuitive interface for managing the scraper.

-   **Refresh List:** Updates the list of scraping jobs.
-   **Add Custom Job:** Opens a modal where you can define and start a new scraping job. You can use the dropdown to pre-fill the form with one of the default targets.
-   **Remove All Jobs:** Deletes all jobs from the database.
-   **Actions (per job):**
    -   **View:** Opens a modal to see the scraped vacancies, with options to view as a formatted list or raw JSON.
    -   **Restart:** Creates and starts a new job with the same source target.
    -   **Remove:** Deletes the specific job.
