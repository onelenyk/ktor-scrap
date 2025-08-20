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
}
