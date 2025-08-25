package dev.onelenyk.ktorscrap.test

import dev.onelenyk.ktorscrap.presentation.routing.ServerRouting
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.http.content.staticResources
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.testModule() {
    install(Koin) {
        slf4jLogger()
        modules(testKoinModule)
    }
    install(CallLogging)
    install(RequestValidation)
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
            },
        )
    }
    val router: ServerRouting by inject()
    routing {
        router.registerRoutes(this)
        staticResources("/", "static") {
            default("index.html")
        }
    }
}
