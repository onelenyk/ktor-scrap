package dev.onelenyk.ktorscrap.features.vacanciesandroid.processor

import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.ScrapeOutput
import dev.onelenyk.ktorscrap.features.vacanciesandroid.repository.ScrapingJobRepository
import dev.onelenyk.ktorscrap.features.vacanciesandroid.repository.model.JobStatus
import dev.onelenyk.ktorscrap.features.vacanciesandroid.repository.model.ScrapingResult
import java.util.UUID

class JobOutputManager(
    private val repository: ScrapingJobRepository,
) {
    suspend fun startProcessing(jobId: UUID) {
        repository.updateJobStatus(
            jobId = jobId,
            status = JobStatus.PROCESSING,
        )
    }

    suspend fun completeJob(
        jobId: UUID,
        result: ScrapeOutput,
    ) {
        val scrapingResult =
            ScrapingResult(
                data = result,
                metadata =
                    mapOf(
                        "scrapedAt" to result.scrapedAt.toString(),
                        "source" to result.source,
                    ),
            )

        repository.updateJobStatus(
            jobId = jobId,
            status = JobStatus.COMPLETED,
            result = scrapingResult,
        )
    }

    suspend fun failJob(
        jobId: UUID,
        error: String,
    ) {
        repository.updateJobStatus(
            jobId = jobId,
            status = JobStatus.FAILED,
            error = error,
        )
    }
}
