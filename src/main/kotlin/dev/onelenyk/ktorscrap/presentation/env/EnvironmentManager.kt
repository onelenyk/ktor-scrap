package dev.onelenyk.ktorscrap.presentation.env

import ru.inforion.lab403.common.logging.TRACE
import ru.inforion.lab403.common.logging.logger

object EnvironmentManager {
    private val log = logger(TRACE)
    private lateinit var envConfig: EnvConfig

    fun load(args: Array<String>) {
        envConfig = EnvConfig.load(args)
        log.info { "Environment loaded: ${envConfig.properties.keys}" }
    }

    fun getPort(): Int {
        return envConfig.get("PORT")?.toInt() ?: 8080
    }

    fun getDbCredentials(): DbCredentials {
        val firestoreProjectId = envConfig.get("FIRESTORE_PROJECT_ID") ?: ""

        return DbCredentials(firestoreProjectId)
    }
}

data class DbCredentials(
    val firestoreProjectId: String,
)
