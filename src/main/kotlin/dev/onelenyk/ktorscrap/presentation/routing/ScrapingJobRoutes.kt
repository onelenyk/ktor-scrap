package dev.onelenyk.ktorscrap.presentation.routing

import dev.onelenyk.ktorscrap.data.repository.ScrapingJobRepository
import dev.onelenyk.ktorscrap.domain.model.ScrapeTarget
import dev.onelenyk.ktorscrap.domain.usecase.JobQueueManager
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import java.util.UUID

fun Route.scrapingJobRoutes(
    repository: ScrapingJobRepository,
    jobQueueManager: JobQueueManager,
) {
    route("/api/jobs") {
        // Get all jobs
        get {
            try {
                val jobs = repository.readAll()
                call.respond(HttpStatusCode.OK, jobs)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to (e.message ?: "Unknown error occurred")),
                )
            }
        }

        // Get job by ID
        get("{id}") {
            try {
                val id =
                    call.parameters["id"] ?: return@get call.respond(
                        HttpStatusCode.BadRequest,
                        mapOf("error" to "Missing job ID"),
                    )

                val job = repository.getById(UUID.fromString(id))
                if (job != null) {
                    call.respond(HttpStatusCode.OK, job)
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Job not found"))
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to (e.message ?: "Unknown error occurred")),
                )
            }
        }

        // Create new job
        post {
            try {
                val target = call.receive<ScrapeTarget>()
                val job = jobQueueManager.addNewJob(target)
                call.respond(HttpStatusCode.Created, job)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to (e.message ?: "Unknown error occurred")),
                )
            }
        }

        // Delete job
        delete("{id}") {
            try {
                val id =
                    call.parameters["id"] ?: return@delete call.respond(
                        HttpStatusCode.BadRequest,
                        mapOf("error" to "Missing job ID"),
                    )

                val success = repository.delete(UUID.fromString(id))
                if (success) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Job not found"))
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to (e.message ?: "Unknown error occurred")),
                )
            }
        }
    }
}
