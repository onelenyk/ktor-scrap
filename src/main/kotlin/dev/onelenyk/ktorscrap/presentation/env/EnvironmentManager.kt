package dev.onelenyk.ktorscrap.presentation.env

import dev.onelenyk.ktorscrap.utils.Base64Decoder
import ru.inforion.lab403.common.logging.TRACE
import ru.inforion.lab403.common.logging.logger

object EnvironmentManager {
    private val log = logger(TRACE)
    private lateinit var envConfig: EnvConfig

    fun load(args: Array<String>) {
        envConfig = EnvConfig.load(args)
        log.info { "Environment loaded: ${envConfig.properties.keys}" }
        validateEnvironmentVariables()
    }

    private fun validateEnvironmentVariables() {
        val requiredKeys = listOf(
            EnvKeys.PORT,
            EnvKeys.FIRESTORE_PROJECT_ID,
            EnvKeys.CONCURRENT_JOB_LIMIT,
            EnvKeys.SERVICE_ACCOUNT_KEY
        )

        val missingKeys = requiredKeys.filter { envConfig.get(it) == null }

        if (missingKeys.isNotEmpty()) {
            val errorMessage =
                "Missing required environment variables: ${missingKeys.joinToString(", ")}"
            log.info { errorMessage }
            throw IllegalStateException(errorMessage)
        }
    }

    fun getPort(): Int {
        return envConfig.get(EnvKeys.PORT)?.toInt() ?: 8080
    }

    fun getDbCredentials(): DbCredentials {
        val firestoreProjectId = envConfig.get(EnvKeys.FIRESTORE_PROJECT_ID)
            ?: throw IllegalStateException("Missing ${EnvKeys.FIRESTORE_PROJECT_ID} environment variable")

        return DbCredentials(firestoreProjectId)
    }

    fun getConcurrentJobLimit(): Int {
        return envConfig.get(EnvKeys.CONCURRENT_JOB_LIMIT)?.toInt() ?: 10
    }

    fun getGcpServiceAccountKey(): String {
        val base64Key = envConfig.get(EnvKeys.SERVICE_ACCOUNT_KEY)
            ?: throw IllegalStateException("Missing ${EnvKeys.SERVICE_ACCOUNT_KEY} environment variable")
        return Base64Decoder.decode(base64Key)
    }
}

data class DbCredentials(
    val firestoreProjectId: String,
)
