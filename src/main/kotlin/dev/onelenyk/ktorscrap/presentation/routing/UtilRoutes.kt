package dev.onelenyk.ktorscrap.presentation.routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.http.content.staticResources
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get

class UtilRoutes() {
    fun registerRoutes(routing: Routing) {
        routing.apply {
            // List all routes
            get("/routes") {
                call.respond(routing.children.map { it.toString() })
            }

            // Health check
            get("/live") { call.respond(HttpStatusCode.OK) }

            // Hello endpoint
            get("/hello") { call.respondText("Hello, Ktor!") }

            // Serve static docs
            staticResources("/", "dokka")
        }
    }
}
