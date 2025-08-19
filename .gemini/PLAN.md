# Project Plan

This document outlines the current features and next steps for the ktor-scrap project.

### Current Features

-   **Multi-Source Scraping System:** A core framework for scraping job vacancies from various websites (e.g., Djinni, Dou).
-   **Job Queue and Processing:** An asynchronous system (`JobQueueManager`, `JobProcessor`) to manage and execute scraping tasks.
-   **Dual Database Integration:**
    -   **MongoDB:** Currently used for storing scraping job data.
    -   **Firestore:** Partially integrated for managing scraper source configurations.
-   **REST API:** A Ktor-based API for controlling and monitoring the application.
-   **Swagger Documentation:** Partial integration of Swagger for API visualization and testing.
-   **Clean Architecture:** The codebase is structured into `data`, `domain`, and `presentation` layers.
-   **Dependency Injection:** Koin is used to manage dependencies throughout the application.
-   **Gradle Build System:** A comprehensive Gradle setup for building, testing, and linting (`ktlint`).

### Next Steps

### 1. Stabilize the Codebase
- **Commit Existing Changes:** The recent refactoring and the addition of the ScraperTypeConfig feature need to be committed to establish a stable baseline.

### 2. Complete Firestore Migration
- **Identify MongoDB Usages:** Find all remaining code that interacts with MongoDB.
- **Replace with Firestore:** Implement the equivalent functionality using Firestore. This will primarily involve the `ScrapingJobRepository`.
- **Remove MongoDB Dependencies:** Once the migration is complete, remove the MongoDB driver and any related configuration.

### 3. Fix and Enable Tests
- **Repair `GeneralRoutesTest`:** Fix the failing tests by using the embedded test server instead of trying to connect to a live server.
- **Review Other Tests:** Ensure other tests are still valid after the refactoring and database migration.
- **Write New Tests:** Add tests for the new `ScraperTypeConfig` feature.

### 4. Code Cleanup
- **Remove Dead Code:** Delete any unused files or code that is a result of the refactoring and migration.
- **Review and Refactor:** Perform a final pass over the codebase to ensure consistency and clarity.