package dev.onelenyk.ktorscrap.presentation.di

import com.google.cloud.firestore.Firestore
import dev.onelenyk.ktorscrap.domain.model.Logger
import org.koin.core.Koin

class SystemHealthChecker(
    private val logger: Logger,
    private val koin: Koin,
    private val firestore: Firestore,
) {
    fun checkAndLog() {
        logger.info("--- System Health Check ---")
        checkKoin()
        checkFirestore()
        logger.info("---------------------------")
    }

    private fun checkKoin() {
        try {
            // Check if a core component can be retrieved
            koin.get<Logger>()
            logger.info("[OK] Koin DI container is active.")
        } catch (e: Exception) {
            logger.error("[FAIL] Koin DI container check failed: ${e.message}")
        }
    }

    private fun checkFirestore() {
        try {
            // A simple, non-blocking check to see if we can interact with Firestore
            firestore.listCollections()
            logger.info("[OK] Firestore connection is active.")
        } catch (e: Exception) {
            logger.error("[FAIL] Firestore connection check failed: ${e.message}")
        }
    }

    suspend fun testFirestoreIntegration(): Boolean {
        logger.info("--- Starting Firestore Integration Test ---")
        val testCollection = "_integration_tests"
        val testDocumentId = "test_doc"
        val testData = mapOf("timestamp" to System.currentTimeMillis(), "test" to "integration")

        return try {
            // Write a document
            logger.info("Writing test document to $testCollection/$testDocumentId...")
            firestore.collection(testCollection).document(testDocumentId).set(testData).get()
            logger.info("Test document written successfully.")

            // Read the document back
            logger.info("Reading test document from $testCollection/$testDocumentId...")
            val snapshot = firestore.collection(testCollection).document(testDocumentId).get().get()

            if (snapshot.exists() && snapshot.data == testData) {
                logger.info("[OK] Firestore integration test passed: Document written and read successfully.")
                true
            } else {
                logger.error("[FAIL] Firestore integration test failed: Document mismatch or not found.")
                false
            }
        } catch (e: Exception) {
            logger.error("[FAIL] Firestore integration test failed with exception: ${e.message}")
            false
        } finally {
            // Clean up: delete the test document
            try {
                firestore.collection(testCollection).document(testDocumentId).delete().get()
                logger.info("Test document $testDocumentId deleted from $testCollection.")
            } catch (e: Exception) {
                logger.warning("Failed to delete test document: ${e.message}")
            }
            logger.info("--- Firestore Integration Test Finished ---")
        }
    }
}
