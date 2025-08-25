package dev.onelenyk.ktorscrap.data.repository

import dev.onelenyk.ktorscrap.data.model.JobStatus
import dev.onelenyk.ktorscrap.data.model.ScrapingJob
import dev.onelenyk.ktorscrap.data.model.ScrapingResult
import dev.onelenyk.ktorscrap.domain.model.ScrapeTarget
import java.util.UUID

class SwitchableScrapingJobRepository(
    private val firestoreRepository: ScrapingJobRepository,
    private val inMemoryRepository: ScrapingJobRepository,
) : ScrapingJobRepository {
    private val repository: ScrapingJobRepository
        get() =
            if (FeatureFlag.useInMemoryRepository) {
                inMemoryRepository
            } else {
                firestoreRepository
            }

    override suspend fun create(job: ScrapingJob): ScrapingJob {
        return repository.create(job)
    }

    override suspend fun getById(id: UUID): ScrapingJob? {
        return repository.getById(id)
    }

    override suspend fun delete(id: UUID): Boolean {
        return repository.delete(id)
    }

    override suspend fun getAll(): List<ScrapingJob> {
        return repository.getAll()
    }

    override suspend fun update(
        id: UUID,
        updatedJob: ScrapingJob,
    ): ScrapingJob? {
        return repository.update(id, updatedJob)
    }

    override suspend fun createJob(source: ScrapeTarget): ScrapingJob {
        return repository.createJob(source)
    }

    override suspend fun updateJobStatus(
        jobId: UUID,
        status: JobStatus,
        result: ScrapingResult?,
        error: String?,
    ): ScrapingJob? {
        return repository.updateJobStatus(jobId, status, result, error)
    }

    override suspend fun deleteAll(): Int {
        return repository.deleteAll()
    }
}
