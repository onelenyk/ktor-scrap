package dev.onelenyk.ktorscrap.domain.usecase

import dev.onelenyk.ktorscrap.data.model.JobStatus
import dev.onelenyk.ktorscrap.data.model.ScrapingResult
import dev.onelenyk.ktorscrap.data.repository.ScrapingJobRepository
import dev.onelenyk.ktorscrap.domain.model.ScrapeOutput
import java.util.UUID

class JobOutputManager(
    private val repository: ScrapingJobRepository,
) {
    suspend fun startProcessing(jobId: String) {
        repository.updateJobStatus(
            jobId = UUID.fromString(jobId),
            status = JobStatus.PROCESSING,
        )
    }

    suspend fun completeJob(
        jobId: String,
        result: ScrapeOutput,
    ) {
        val scrapingResult =
            ScrapingResult(
                data = result,
                metadata =
                    mapOf(
                        "vacancies" to result.vacancies.toString(),
                        "source" to result.source,
                    ),
            )

        repository.updateJobStatus(
            jobId = UUID.fromString(jobId),
            status = JobStatus.COMPLETED,
            result = scrapingResult,
        )
    }

    suspend fun failJob(
        jobId: String,
        error: String,
    ) {
        repository.updateJobStatus(
            jobId = UUID.fromString(jobId),
            status = JobStatus.FAILED,
            error = error,
        )
    }
}
