package dev.onelenyk.ktorscrap.app.env

import ru.inforion.lab403.common.logging.TRACE
import ru.inforion.lab403.common.logging.logger

object EnvironmentManager {
    private val log = logger(TRACE)

    fun getPort(): Int {
        return System.getenv("PORT")?.toInt() ?: 8080
    }

    fun getDbCredentials(): DbCredentials {
        val username = System.getenv("DB_USERNAME") ?: ""
        val password = System.getenv("DB_PASSWORD") ?: ""
        val connection = System.getenv("DB_CONNECTION") ?: ""
        
        if (username.isBlank() || password.isBlank() || connection.isBlank()) {
            log.warning { "Missing database credentials in environment variables" }
            throw IllegalStateException("Missing database credentials")
        }
        
        return DbCredentials(username, password, connection)
    }
}

data class DbCredentials(
    val username: String,
    val password: String,
    val connection: String
) 