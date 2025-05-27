package dev.onelenyk.ktorscrap.features.vacanciesandroid.model

import kotlinx.serialization.Serializable

@Serializable
data class ScrapeOutput(
    val source: String,
    val vacancies: List<Vacancy>,
    val target: ScrapeTarget,
    val scrapedAt: Long = System.currentTimeMillis(),
)
