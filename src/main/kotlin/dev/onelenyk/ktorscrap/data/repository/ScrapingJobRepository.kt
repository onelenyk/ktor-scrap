package dev.onelenyk.ktorscrap.data.repository

import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.kotlin.client.coroutine.MongoCollection
import dev.onelenyk.ktorscrap.data.db.MongoDBManager
import dev.onelenyk.ktorscrap.data.model.JobStatus
import dev.onelenyk.ktorscrap.data.model.ScrapingJob
import dev.onelenyk.ktorscrap.data.model.ScrapingResult
import dev.onelenyk.ktorscrap.domain.model.ScrapeTarget
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.Document
import java.time.Instant
import java.util.UUID

interface ScrapingJobRepository {
    suspend fun create(job: ScrapingJob): ScrapingJob

    suspend fun getById(id: UUID): ScrapingJob?

    suspend fun delete(id: UUID): Boolean

    suspend fun readAll(): List<ScrapingJob>

    suspend fun update(
        id: UUID,
        document: Document,
    ): ScrapingJob?

    suspend fun createJob(source: ScrapeTarget): ScrapingJob

    suspend fun updateJobStatus(
        jobId: UUID,
        status: JobStatus,
        result: ScrapingResult? = null,
        error: String? = null,
    ): ScrapingJob?
}

class ScrapingJobRepositoryImpl(
    private val mongoDBManager: MongoDBManager,
) : ScrapingJobRepository {
    private val collection: MongoCollection<ScrapingJob> =
        mongoDBManager.getCollection(
            "scraping_jobs",
            ScrapingJob::class.java,
        )

    override suspend fun create(job: ScrapingJob): ScrapingJob {
        val insertOne = collection.insertOne(job)
        val filter = Filters.eq("_id", insertOne.insertedId)
        return collection.find(filter).first()
    }

    override suspend fun getById(id: UUID): ScrapingJob? {
        val filter = Filters.eq("_id", id)
        return collection.find(filter).firstOrNull()
    }

    override suspend fun delete(id: UUID): Boolean {
        val filter = Filters.eq("_id", id)
        val result = collection.deleteOne(filter)
        return result.deletedCount > 0
    }

    override suspend fun readAll(): List<ScrapingJob> {
        return collection.find().toList()
    }

    override suspend fun update(
        id: UUID,
        document: Document,
    ): ScrapingJob? {
        val filter = Filters.eq("_id", id)
        collection.updateOne(filter, document, UpdateOptions().upsert(true))
        return getById(id)
    }

    // Helper methods for common operations
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
        val update =
            Document().apply {
                put("status", status.name)
                put("updatedAt", Instant.now().epochSecond)
                if (result != null) put("result", result)
                if (error != null) put("error", error)
            }
        return update(jobId, Document("\$set", update))
    }
}
