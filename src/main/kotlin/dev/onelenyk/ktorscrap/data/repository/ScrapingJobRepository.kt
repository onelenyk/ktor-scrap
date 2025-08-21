package dev.onelenyk.ktorscrap.data.repository

import dev.onelenyk.ktorscrap.data.model.JobStatus
import dev.onelenyk.ktorscrap.data.model.ScrapingJob
import dev.onelenyk.ktorscrap.data.model.ScrapingResult
import dev.onelenyk.ktorscrap.domain.model.ScrapeTarget
import java.util.UUID

interface ScrapingJobRepository {
    suspend fun create(job: ScrapingJob): ScrapingJob

    suspend fun getById(id: UUID): ScrapingJob?

    suspend fun delete(id: UUID): Boolean

    suspend fun getAll(): List<ScrapingJob>

    suspend fun update(
        id: UUID,
        updatedJob: ScrapingJob,
    ): ScrapingJob?

    suspend fun createJob(source: ScrapeTarget): ScrapingJob

    suspend fun updateJobStatus(
        jobId: UUID,
        status: JobStatus,
        result: ScrapingResult? = null,
        error: String? = null,
    ): ScrapingJob?

    suspend fun deleteAll(): Int
}
