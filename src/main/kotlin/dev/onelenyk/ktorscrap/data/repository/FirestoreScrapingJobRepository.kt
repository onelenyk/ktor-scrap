package dev.onelenyk.ktorscrap.data.repository

import dev.onelenyk.ktorscrap.data.db.Database
import dev.onelenyk.ktorscrap.data.model.JobStatus
import dev.onelenyk.ktorscrap.data.model.ScrapingJob
import dev.onelenyk.ktorscrap.data.model.ScrapingResult
import dev.onelenyk.ktorscrap.domain.model.ScrapeTarget
import java.util.UUID

class FirestoreScrapingJobRepository(
    private val firestoreService: Database<ScrapingJob>,
) : ScrapingJobRepository {
    override suspend fun create(job: ScrapingJob): ScrapingJob {
        return firestoreService.create(job)
    }

    override suspend fun getById(id: UUID): ScrapingJob? {
        return firestoreService.getById(id.toString())
    }

    override suspend fun delete(id: UUID): Boolean {
        firestoreService.delete(id.toString())
        return true // Firestore delete doesn't return a boolean
    }

    override suspend fun readAll(): List<ScrapingJob> {
        return firestoreService.getAll()
    }

    override suspend fun update(
        id: UUID,
        document: org.bson.Document,
    ): ScrapingJob? {
        // This method is specific to MongoDB and needs to be adapted for Firestore.
        // For now, it will not be implemented.
        throw UnsupportedOperationException("Update with BSON Document is not supported for Firestore.")
    }

    override suspend fun createJob(source: ScrapeTarget): ScrapingJob {
        val job = ScrapingJob(source = source)
        return create(job)
    }

    override suspend fun updateJobStatus(
        jobId: UUID,
        status: JobStatus,
        result: ScrapingResult?,
        error: String?,
    ): ScrapingJob? {
        val job = getById(jobId) ?: return null
        val updatedJob =
            job.copy(
                status = status,
                result = result,
                error = error,
            )
        return firestoreService.update(updatedJob)
    }
}
