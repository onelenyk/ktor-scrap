package dev.onelenyk.ktorscrap.presentation.env

import java.io.File

data class EnvConfig(val properties: Map<String, String>) {
    fun get(key: String): String? = properties[key]

    companion object {
        fun load(args: Array<String>): EnvConfig {
            val loadedProperties = mutableMapOf<String, String>()

            // 1. Load from system environment variables
            System.getenv().forEach { (key, value) ->
                loadedProperties[key] = value
            }

            // 2. Load from .env file
            val dotEnvFile = File(".env")
            if (dotEnvFile.exists()) {
                dotEnvFile.readLines().forEach { line ->
                    val trimmedLine = line.trim()
                    if (trimmedLine.isNotEmpty() && !trimmedLine.startsWith("#")) {
                        val parts = trimmedLine.split("=", limit = 2)
                        if (parts.size == 2) {
                            loadedProperties[parts[0].trim()] = parts[1].trim()
                        }
                    }
                }
            }

            // 3. Load from system properties (e.g., -Dkey=value)
            System.getProperties().forEach { (key, value) ->
                if (key is String && value is String) {
                    loadedProperties[key] = value
                }
            }

            // 4. Load from command-line arguments (e.g., --key=value)
            args.forEach { arg ->
                if (arg.startsWith("--")) {
                    val parts = arg.substring(2).split("=", limit = 2)
                    if (parts.size == 2) {
                        loadedProperties[parts[0].trim()] = parts[1].trim()
                    }
                }
            }

            return EnvConfig(loadedProperties)
        }
    }
}
