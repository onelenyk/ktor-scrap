package dev.onelenyk.ktorscrap.features.vacanciesandroid.scraper

import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.Logger
import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.ScrapeOutput
import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.ScrapeTarget
import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.Vacancy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class LeverScraper : JobScraper {
    override suspend fun scrape(
        target: ScrapeTarget,
        logger: Logger,
    ): ScrapeOutput {
        logger.info("[Lever] Starting to scrape: ${target.url}")
        val doc =
            withContext(Dispatchers.IO) {
                Jsoup.connect(target.url)
                    .userAgent("Mozilla/5.0")
                    .get()
            }
        logger.info("[Lever] Successfully connected to the page")

        return parseLeverJobs(doc, target.name, target, logger)
    }

    private fun parseLeverJobs(
        doc: Document,
        companyName: String,
        target: ScrapeTarget,
        logger: Logger,
    ): ScrapeOutput {
        val jobs = mutableListOf<Vacancy>()
        val jobCards = doc.select("div.posting")
        logger.info("[Lever] Found ${jobCards.size} job cards")

        jobCards.forEach { card ->
            try {
                val title = card.select("h5").text()
                val location = card.select("span.sort-by-location").text()
                val url = card.select("a.posting-title").attr("href")
                val department = card.select("span.sort-by-team").text()

                jobs.add(
                    Vacancy(
                        title = title,
                        company = companyName,
                        location = location,
                        description = "Department: $department",
                        url = url,
                        source = "Lever",
                    ),
                )
                logger.info("[Lever] Successfully parsed job: $title at $companyName")
            } catch (e: Exception) {
                logger.error("[Lever] Error parsing job card: ${e.message}", e)
            }
        }

        logger.info("[Lever] Successfully parsed ${jobs.size} jobs")
        return ScrapeOutput.withFiltering(companyName, jobs, target)
    }
}
