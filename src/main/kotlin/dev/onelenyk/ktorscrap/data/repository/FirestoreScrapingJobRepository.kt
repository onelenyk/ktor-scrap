package dev.onelenyk.ktorscrap.data.repository

import com.google.cloud.firestore.Firestore
import dev.onelenyk.ktorscrap.data.model.JobStatus
import dev.onelenyk.ktorscrap.data.model.ScrapingJob
import dev.onelenyk.ktorscrap.data.model.ScrapingJobMapper
import dev.onelenyk.ktorscrap.data.model.ScrapingResult
import dev.onelenyk.ktorscrap.domain.model.ScrapeTarget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.util.UUID

class FirestoreScrapingJobRepository(
    private val firestore: Firestore,
) : ScrapingJobRepository {
    private val collection = firestore.collection("scraping_jobs")

    override suspend fun create(job: ScrapingJob): ScrapingJob {
        withContext(Dispatchers.IO) {
            collection.document(job.id).set(ScrapingJobMapper.toDocument(job)).get()
        }
        return job
    }

    override suspend fun getById(id: UUID): ScrapingJob? =
        withContext(Dispatchers.IO) {
            val snapshot = collection.document(id.toString()).get().get()
            if (snapshot.exists()) {
                ScrapingJobMapper.fromDocument(snapshot)
            } else {
                null
            }
        }

    override suspend fun delete(id: UUID): Boolean {
        return withContext(Dispatchers.IO) {
            collection.document(id.toString()).delete().get()
            true
        }
    }

    override suspend fun getAll(): List<ScrapingJob> =
        withContext(Dispatchers.IO) {
            val snapshot = collection.get().get()
            snapshot.documents.map { ScrapingJobMapper.fromDocument(it) }
        }

    override suspend fun update(
        id: UUID,
        updatedJob: ScrapingJob,
    ): ScrapingJob? {
        withContext(Dispatchers.IO) {
            collection.document(id.toString()).set(ScrapingJobMapper.toDocument(updatedJob)).get()
        }
        return updatedJob
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
        val existingJob = getById(jobId) ?: return null
        val updatedJob =
            existingJob.copy(
                status = status,
                result = result,
                error = error,
                updatedAt = Instant.now().epochSecond,
            )
        return update(jobId, updatedJob)
    }
}
