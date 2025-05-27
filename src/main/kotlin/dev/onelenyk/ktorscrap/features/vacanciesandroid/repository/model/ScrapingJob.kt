package dev.onelenyk.ktorscrap.features.vacanciesandroid.repository.model

import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.ScrapeOutput
import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.ScrapeTarget
import dev.onelenyk.ktorscrap.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import java.time.Instant
import java.util.UUID

@Serializable
data class ScrapingResult(
    val data: ScrapeOutput,
    val metadata: Map<String, String> = emptyMap(),
)

@Serializable
data class ScrapingJob(
    @Serializable(with = UUIDSerializer::class) @BsonId val id: UUID = UUID.randomUUID(),
    val source: ScrapeTarget,
    val status: JobStatus = JobStatus.PENDING,
    val createdAt: Long = Instant.now().epochSecond,
    val updatedAt: Long = Instant.now().epochSecond,
    val result: ScrapingResult? = null,
    val error: String? = null,
)

@Serializable
enum class JobStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED,
}
