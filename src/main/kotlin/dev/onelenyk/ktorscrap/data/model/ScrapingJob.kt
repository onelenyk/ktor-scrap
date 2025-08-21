package dev.onelenyk.ktorscrap.data.model

import dev.onelenyk.ktorscrap.data.db.Identifiable
import dev.onelenyk.ktorscrap.domain.model.ScrapeOutput
import dev.onelenyk.ktorscrap.domain.model.ScrapeTarget
import dev.onelenyk.ktorscrap.utils.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
data class ScrapingResult(
    val data: ScrapeOutput,
    val metadata: Map<String, String> = emptyMap(),
)

@Serializable
data class ScrapingJob(
    @Serializable(with = UUIDSerializer::class) @Contextual val uuid: UUID = UUID.randomUUID(),
    val source: ScrapeTarget,
    val status: JobStatus = JobStatus.PENDING,
    val createdAt: Long = Instant.now().epochSecond,
    val updatedAt: Long = Instant.now().epochSecond,
    val result: ScrapingResult? = null,
    val error: String? = null,
) : Identifiable {
    override val id: String
        get() = uuid.toString()
}

@Serializable
enum class JobStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED,
}
