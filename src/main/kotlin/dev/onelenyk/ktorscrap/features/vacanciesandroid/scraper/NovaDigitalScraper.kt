package dev.onelenyk.ktorscrap.features.vacanciesandroid.scraper

import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.Logger
import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.ScrapeOutput
import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.ScrapeTarget
import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.Vacancy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class NovaDigitalScraper : JobScraper {
    override suspend fun scrape(
        target: ScrapeTarget,
        logger: Logger,
    ): ScrapeOutput {
        logger.info("[NovaDigital] Starting to scrape: ${target.url}")
        val doc =
            withContext(Dispatchers.IO) {
                Jsoup.connect(target.url).userAgent("Mozilla/5.0").get()
            }
        logger.info("[NovaDigital] Successfully connected to the page")
        return parseNovaDigitalJobs(doc, target.name, target, logger)
    }

    private fun parseNovaDigitalJobs(
        doc: Document,
        companyName: String,
        target: ScrapeTarget,
        logger: Logger,
    ): ScrapeOutput {
        val jobs = mutableListOf<Vacancy>()
        val jobCards = doc.select("div.career-card, div.vacancy, div.job-card")
        logger.info("[NovaDigital] Found ${jobCards.size} job cards")
        jobCards.forEach { card ->
            try {
                val title =
                    card.select(".career-title, .vacancy-title, .job-title, h2, h3").firstOrNull()
                        ?.text() ?: ""
                val location =
                    card.select(".career-location, .vacancy-location, .job-location, .location")
                        .firstOrNull()?.text() ?: ""
                val department =
                    card.select(".career-department, .vacancy-department, .job-department, .department")
                        .firstOrNull()?.text() ?: ""
                val url = card.select("a[href]").firstOrNull()?.absUrl("href") ?: ""
                jobs.add(
                    Vacancy(
                        title = title,
                        company = companyName,
                        location = location,
                        description = if (department.isNotBlank()) "Department: $department" else "",
                        url = url,
                        source = "Nova Digital",
                    ),
                )
                logger.info("[NovaDigital] Successfully parsed job: $title at $companyName")
            } catch (e: Exception) {
                logger.error("[NovaDigital] Error parsing job card: ${e.message}", e)
            }
        }
        logger.info("[NovaDigital] Successfully parsed ${jobs.size} jobs")
        return ScrapeOutput.withFiltering(companyName, jobs, target)
    }
}
