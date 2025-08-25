package dev.onelenyk.ktorscrap.data.repository

import dev.onelenyk.ktorscrap.data.model.JobStatus
import dev.onelenyk.ktorscrap.data.model.ScrapingJob
import dev.onelenyk.ktorscrap.data.model.ScrapingResult
import dev.onelenyk.ktorscrap.domain.model.ScrapeTarget
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class InMemoryScrapingJobRepository : ScrapingJobRepository {
    private val jobs = ConcurrentHashMap<UUID, ScrapingJob>()

    override suspend fun create(job: ScrapingJob): ScrapingJob {
        jobs[job.uuid] = job
        return job
    }

    override suspend fun getById(id: UUID): ScrapingJob? {
        return jobs[id]
    }

    override suspend fun delete(id: UUID): Boolean {
        return jobs.remove(id) != null
    }

    override suspend fun getAll(): List<ScrapingJob> {
        return jobs.values.toList()
    }

    override suspend fun update(
        id: UUID,
        updatedJob: ScrapingJob,
    ): ScrapingJob? {
        return if (jobs.containsKey(id)) {
            jobs[id] = updatedJob
            updatedJob
        } else {
            null
        }
    }

    override suspend fun createJob(source: ScrapeTarget): ScrapingJob {
        val job =
            ScrapingJob(
                uuid = UUID.randomUUID(),
                source = source,
                status = JobStatus.PENDING,
                createdAt = System.currentTimeMillis(),
            )
        create(job)
        return job
    }

    override suspend fun updateJobStatus(
        jobId: UUID,
        status: JobStatus,
        result: ScrapingResult?,
        error: String?,
    ): ScrapingJob? {
        val job = getById(jobId)
        return if (job != null) {
            val updatedJob =
                job.copy(
                    status = status,
                    result = result,
                    error = error,
                )
            update(jobId, updatedJob)
        } else {
            null
        }
    }

    override suspend fun deleteAll(): Int {
        val count = jobs.size
        jobs.clear()
        return count
    }
}
