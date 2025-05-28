package dev.onelenyk.ktorscrap.app

import dev.onelenyk.ktorscrap.app.di.koinModule
import dev.onelenyk.ktorscrap.app.env.EnvironmentManager
import dev.onelenyk.ktorscrap.app.plugins.configureSwagger
import dev.onelenyk.ktorscrap.app.routing.ServerRouting
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import ru.inforion.lab403.common.logging.TRACE
import ru.inforion.lab403.common.logging.logger
import java.net.InetAddress

class Server {
    val log = logger(TRACE)

    fun start(): NettyApplicationEngine {
        log.config { "Starting Ktor server initialization..." }
        
        val port = EnvironmentManager.getPort()
        log.info { "Configuring server on port: $port" }

        val ip = InetAddress.getLocalHost().hostAddress
        log.info { "Server IP address: $ip" }

        val server =
            embeddedServer(Netty, port = port) {
                module(this)
            }

        try {
            log.severe { "Starting server..." }
            server.start(wait = true)
            log.severe { "Server successfully started on port: $port" }
        } catch (e: Exception) {
            log.warning { "Failed to start server: ${e.message}" }
            throw e
        }
        return server
    }

    fun module(application: Application) =
        application.apply {
            log.fine { "Configuring application modules..." }

            log.fine { "Installing Koin dependency injection..." }
            install(Koin) {
                slf4jLogger()
                modules(koinModule)
            }

            log.fine { "Setting up request logging..." }
            install(CallLogging)

            log.fine { "Configuring request validation..." }
            install(RequestValidation)

            log.finest { "Setting up content serialization..." }
            configureSerialization()

            log.finest { "Configuring Swagger..." }
            configureSwagger()

            log.finest { "Configuring routing..." }
            configureRouting()

            log.finest { "Application modules configuration completed" }
        }

    private fun Application.configureSerialization() =
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                },
            )
        }

    private fun Application.configureRouting() {
        val router: ServerRouting by inject()

        routing {
            router.registerRoutes(this)
        }
    }
}
