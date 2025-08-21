package dev.onelenyk.ktorscrap.presentation.routing

import dev.onelenyk.ktorscrap.domain.usecase.JobQueueManager
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

class DefaultJobsRoutes(
    private val jobQueueManager: JobQueueManager,
) {
    fun registerRoutes(routing: Route) {
        routing.route("/api/default-jobs") {
            get("/add-all") {
                val addedJobs = mutableListOf<String>()
                VacancyTargets.targets.forEach { target ->
                    val job = jobQueueManager.addNewJob(target)
                    addedJobs.add(job.uuid.toString())
                }
                call.respond(
                    HttpStatusCode.OK,
                    mapOf("status" to "All default jobs added to queue", "added_job_ids" to addedJobs),
                )
            }
        }
    }
}
