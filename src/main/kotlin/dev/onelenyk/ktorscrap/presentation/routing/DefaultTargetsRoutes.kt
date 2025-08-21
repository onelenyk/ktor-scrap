package dev.onelenyk.ktorscrap.presentation.routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.defaultTargetsRoutes() {
    route("/api/default-targets") {
        get {
            try {
                val targets = VacancyTargets.targets
                call.respond(HttpStatusCode.OK, targets)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to (e.message ?: "Unknown error occurred")),
                )
            }
        }
    }
}
