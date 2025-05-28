package dev.onelenyk.ktorscrap.app

fun main(args: Array<String>) {
    // Parse command line arguments
    args.forEach { arg ->
        if (arg.startsWith("--env=")) {
            val envVar = arg.substringAfter("--env=").split("==")
            if (envVar.size == 2) {
                val key = envVar[0]
                val value = envVar[1]
                System.setProperty(key, value)
                println("Set environment variable: $key=$value")
            }
        }
    }

    // Example usage:
    // --env=PORT==8080 --env=DB_USERNAME==user --env=DB_PASSWORD==pass --env=DB_CONNECTION==connection_string

    val server = Server()
    server.start()
}
