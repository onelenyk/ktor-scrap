# Project Plan

This document outlines the current features and next steps for the ktor-scrap project.

### Current Features

-   **Multi-Source Scraping System:** A core framework for scraping job vacancies from various websites (e.g., Djinni, Dou).
-   **Job Queue and Processing:** An asynchronous system (`JobQueueManager`, `JobProcessor`) to manage and execute scraping tasks.
-   **Firestore Integration:** The application is fully migrated to use Firestore for all database operations.
-   **REST API:** A Ktor-based API for controlling and monitoring the application.
-   **Swagger Documentation:** Partial integration of Swagger for API visualization and testing.
-   **Clean Architecture:** The codebase is structured into `data`, `domain`, and `presentation` layers.
-   **Dependency Injection:** Koin is used to manage dependencies throughout the application.
-   **Gradle Build System:** A comprehensive Gradle setup for building, testing, and linting (`ktlint`).
-   **Default Jobs Route:** An API endpoint (`/api/default-jobs/add-all`) to easily queue up a predefined list of scraping targets.

### Completed Tasks

-   **Firestore Migration:** The project has been fully migrated from MongoDB to Firestore. This included implementing a custom `ScrapingJobMapper` to handle the serialization and deserialization of `ScrapingJob` objects, removing the dependency on the generic `FirestoreService` for this data type.
-   **Fixed and Enabled Tests:** All tests are now passing. This was achieved by initializing the `EnvironmentManager` correctly in the test environment.
-   **Code Cleanup:**
    -   Renamed `VacancyRoutes.kt` to `VacancyTargets.kt` to better reflect its contents.
    -   Refactored `VacancyRoutes` to be `DefaultJobsRoutes` and moved the routing logic to a new file.
    -   Removed unused code and dependencies related to MongoDB.
-   **Resolved Firestore Deserialization Issues:** Fixed warnings and errors related to Firestore deserialization by adding missing fields to `ScraperTypeConfig` and implementing a custom mapper for `ScrapingJob`.

### Next Steps

-   **Review and Refactor:** Perform a final pass over the codebase to ensure consistency and clarity.
-   **Write New Tests:** Add tests for the new `DefaultJobsRoutes` and the `ScraperTypeConfig` feature.
-   **Commit Changes:** Commit the recent changes to establish a new stable baseline.
