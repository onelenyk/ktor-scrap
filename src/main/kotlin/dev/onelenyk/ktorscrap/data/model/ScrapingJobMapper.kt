package dev.onelenyk.ktorscrap.data.model

import com.google.cloud.firestore.DocumentSnapshot
import dev.onelenyk.ktorscrap.domain.model.ScrapeOutput
import dev.onelenyk.ktorscrap.domain.model.ScrapeTarget
import dev.onelenyk.ktorscrap.domain.model.ScraperType
import dev.onelenyk.ktorscrap.domain.model.Vacancy
import java.util.UUID

object ScrapingJobMapper {
    private object VacancyMapper {
        fun fromMap(map: Map<String, Any>): Vacancy {
            return Vacancy(
                title = map["title"] as String,
                company = map["company"] as String,
                location = map["location"] as String,
                salary = map["salary"] as? String,
                description = map["description"] as String,
                url = map["url"] as String,
                source = map["source"] as String,
            )
        }
    }

    fun fromDocument(doc: DocumentSnapshot): ScrapingJob {
        val sourceMap = doc.get("source") as Map<String, Any>
        val source =
            ScrapeTarget(
                url = sourceMap["url"] as String,
                name = sourceMap["name"] as String,
                needsFiltering = sourceMap["needsFiltering"] as Boolean,
                scraperType = ScraperType.valueOf(sourceMap["scraperType"] as String),
            )

        val resultMap = doc.get("result") as? Map<String, Any>
        val result =
            resultMap?.let {
                val dataMap = it["data"] as Map<String, Any>
                val vacanciesList = dataMap["vacancies"] as List<Map<String, Any>>
                val vacancies = vacanciesList.map { vacancyMap -> VacancyMapper.fromMap(vacancyMap) }
                val targetMap = dataMap["target"] as Map<String, Any>
                val target =
                    ScrapeTarget(
                        url = targetMap["url"] as String,
                        name = targetMap["name"] as String,
                        needsFiltering = targetMap["needsFiltering"] as Boolean,
                        scraperType = ScraperType.valueOf(targetMap["scraperType"] as String),
                    )
                val data =
                    ScrapeOutput(
                        source = dataMap["source"] as String,
                        vacancies = vacancies,
                        target = target,
                        scrapedAt = dataMap["scrapedAt"] as Long,
                    )
                val metadata = it["metadata"] as? Map<String, String> ?: emptyMap()
                ScrapingResult(data, metadata)
            }

        return ScrapingJob(
            uuid = UUID.fromString(doc.id),
            source = source,
            status = JobStatus.valueOf(doc.getString("status")!!),
            createdAt = doc.getLong("createdAt")!!,
            updatedAt = doc.getLong("updatedAt")!!,
            result = result,
            error = doc.getString("error"),
        )
    }

    fun toDocument(job: ScrapingJob): Map<String, Any?> {
        return mapOf(
            "uuid" to job.uuid.toString(),
            "source" to
                mapOf(
                    "url" to job.source.url,
                    "name" to job.source.name,
                    "needsFiltering" to job.source.needsFiltering,
                    "scraperType" to job.source.scraperType.name,
                ),
            "status" to job.status.name,
            "createdAt" to job.createdAt,
            "updatedAt" to job.updatedAt,
            "result" to
                job.result?.let {
                    mapOf(
                        "data" to
                            mapOf(
                                "source" to it.data.source,
                                "vacancies" to
                                    it.data.vacancies.map { vacancy ->
                                        mapOf(
                                            "title" to vacancy.title,
                                            "company" to vacancy.company,
                                            "location" to vacancy.location,
                                            "salary" to vacancy.salary,
                                            "description" to vacancy.description,
                                            "url" to vacancy.url,
                                            "source" to vacancy.source,
                                        )
                                    },
                                "target" to
                                    mapOf(
                                        "url" to it.data.target.url,
                                        "name" to it.data.target.name,
                                        "needsFiltering" to it.data.target.needsFiltering,
                                        "scraperType" to it.data.target.scraperType.name,
                                    ),
                                "scrapedAt" to it.data.scrapedAt,
                            ),
                        "metadata" to it.metadata,
                    )
                },
            "error" to job.error,
        )
    }
}
