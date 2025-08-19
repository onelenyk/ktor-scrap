package dev.onelenyk.ktorscrap.presentation.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Application.configureSwagger() {
    routing {
        swaggerUI(path = "swagger-ui", swaggerFile = "api-spec.yaml")
        get("/openapi.yaml") {
            val specFile = File("api-spec.yaml")
            if (specFile.exists()) {
                call.respondText(specFile.readText(), contentType = ContentType("text", "yaml"))
            } else {
                call.respondText("OpenAPI specification not found", status = HttpStatusCode.NotFound)
            }
        }
    }
}
