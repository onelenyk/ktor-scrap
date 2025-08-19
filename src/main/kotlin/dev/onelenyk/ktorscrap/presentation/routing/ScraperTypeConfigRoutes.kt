package dev.onelenyk.ktorscrap.presentation.routing

import dev.onelenyk.ktorscrap.data.repository.ScraperTypeConfigRepository
import dev.onelenyk.ktorscrap.domain.model.ScraperTypeConfig
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Route.scraperTypeConfigRoutes(repository: ScraperTypeConfigRepository) {
    route("/api/scraper-types") {
        get {
            try {
                val scraperTypes = repository.getAll()
                call.respond(HttpStatusCode.OK, scraperTypes)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to (e.message ?: "Unknown error occurred")),
                )
            }
        }

        get("{id}") {
            try {
                val id =
                    call.parameters["id"] ?: return@get call.respond(
                        HttpStatusCode.BadRequest,
                        mapOf("error" to "Missing scraper type ID"),
                    )
                val scraperType = repository.getById(id)
                if (scraperType != null) {
                    call.respond(HttpStatusCode.OK, scraperType)
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Scraper type not found"))
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to (e.message ?: "Unknown error occurred")),
                )
            }
        }

        post {
            try {
                val scraperType = call.receive<ScraperTypeConfig>()
                val created = repository.create(scraperType)
                call.respond(HttpStatusCode.Created, created)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to (e.message ?: "Unknown error occurred")),
                )
            }
        }

        put("{id}") {
            try {
                val id =
                    call.parameters["id"] ?: return@put call.respond(
                        HttpStatusCode.BadRequest,
                        mapOf("error" to "Missing scraper type ID"),
                    )
                val scraperType = call.receive<ScraperTypeConfig>()
                if (id != scraperType.id) {
                    return@put call.respond(
                        HttpStatusCode.BadRequest,
                        mapOf("error" to "ID in path does not match ID in body"),
                    )
                }
                val updated = repository.update(scraperType)
                call.respond(HttpStatusCode.OK, updated)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to (e.message ?: "Unknown error occurred")),
                )
            }
        }

        delete("{id}") {
            try {
                val id =
                    call.parameters["id"] ?: return@delete call.respond(
                        HttpStatusCode.BadRequest,
                        mapOf("error" to "Missing scraper type ID"),
                    )
                repository.delete(id)
                call.respond(HttpStatusCode.NoContent)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to (e.message ?: "Unknown error occurred")),
                )
            }
        }
    }
}
