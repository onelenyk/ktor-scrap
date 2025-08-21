package dev.onelenyk.ktorscrap.presentation.routing

import dev.onelenyk.ktorscrap.data.repository.ScraperTypeConfigRepository
import dev.onelenyk.ktorscrap.data.repository.ScrapingJobRepository
import dev.onelenyk.ktorscrap.domain.usecase.JobQueueManager
import io.ktor.server.routing.Routing

class ServerRouting(
    val repository: ScrapingJobRepository,
    val jobQueueManager: JobQueueManager,
    val scraperTypeConfigRepository: ScraperTypeConfigRepository,
    val utilRoutes: UtilRoutes,
    val defaultJobsRoutes: DefaultJobsRoutes,
) {
    fun registerRoutes(routing: Routing) {
        utilRoutes.registerRoutes(routing)
        routing.scrapingJobRoutes(repository, jobQueueManager)
        //  routing.scraperTypeConfigRoutes(scraperTypeConfigRepository)
        defaultJobsRoutes.registerRoutes(routing)
    }
}
