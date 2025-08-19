package dev.onelenyk.ktorscrap.presentation.env

import ru.inforion.lab403.common.logging.TRACE
import ru.inforion.lab403.common.logging.logger

object EnvironmentManager {
    private val log = logger(TRACE)

    fun getPort(): Int {
        return getEnvOrProperty("PORT")?.toInt() ?: 8080
    }

    fun getDbCredentials(): DbCredentials {
        val username = getEnvOrProperty("DB_USERNAME") ?: ""
        val password = getEnvOrProperty("DB_PASSWORD") ?: ""
        val connection = getEnvOrProperty("DB_CONNECTION") ?: ""
        val firestoreProjectId = getEnvOrProperty("FIRESTORE_PROJECT_ID") ?: ""

        if (username.isBlank() || password.isBlank() || connection.isBlank()) {
            log.warning { "Missing database credentials in environment variables" }
        }

        return DbCredentials(username, password, connection, firestoreProjectId)
    }

    private fun getEnvOrProperty(key: String): String? {
        return System.getenv(key) ?: System.getProperty(key)
    }
}

data class DbCredentials(
    val username: String,
    val password: String,
    val connection: String,
    val firestoreProjectId: String,
)
