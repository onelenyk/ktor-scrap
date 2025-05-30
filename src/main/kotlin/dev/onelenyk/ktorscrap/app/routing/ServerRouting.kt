package dev.onelenyk.ktorscrap.app.routing

import dev.onelenyk.ktorscrap.features.vacanciesandroid.processor.JobQueueManager
import dev.onelenyk.ktorscrap.features.vacanciesandroid.repository.ScrapingJobRepository
import dev.onelenyk.ktorscrap.features.vacanciesandroid.routing.vacancyRoutes
import io.ktor.server.routing.Routing

class ServerRouting(
    val repository: ScrapingJobRepository,
    val jobQueueManager: JobQueueManager,
) {
    private val utilRoutes = UtilRoutes()

    fun registerRoutes(routing: Routing) {
        utilRoutes.registerRoutes(routing)
        routing.vacancyRoutes()
        routing.scrapingJobRoutes(repository, jobQueueManager)
    }
}
