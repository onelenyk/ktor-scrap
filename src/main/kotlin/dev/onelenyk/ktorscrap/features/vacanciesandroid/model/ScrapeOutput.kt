package dev.onelenyk.ktorscrap.features.vacanciesandroid.model

import kotlinx.serialization.Serializable

@Serializable
data class ScrapeOutput(
    val source: String,
    val vacancies: List<Vacancy>,
    val target: ScrapeTarget,
    val scrapedAt: Long = System.currentTimeMillis(),
) {
    companion object {
        fun withFiltering(
            source: String,
            vacancies: List<Vacancy>,
            target: ScrapeTarget,
            scrapedAt: Long = System.currentTimeMillis(),
        ) = ScrapeOutput(
            source = source,
            vacancies =
                if (target.needsFiltering) {
                    vacancies.filter { VacancyFilter.isRelevant(it) }
                } else {
                    vacancies
                },
            target = target,
            scrapedAt = scrapedAt,
        )
    }
}
