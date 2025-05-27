package dev.onelenyk.ktorscrap.features.vacanciesandroid.scraper

import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.Logger
import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.ScrapeOutput
import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.ScrapeTarget
import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.Vacancy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URI

class BreezyScraper : JobScraper {
    override suspend fun scrape(
        target: ScrapeTarget,
        logger: Logger,
    ): ScrapeOutput {
        logger.info("[Breezy] Starting to scrape: ${target.url}")
        val doc =
            withContext(Dispatchers.IO) {
                Jsoup.connect(target.url)
                    .userAgent("Mozilla/5.0")
                    .get()
            }
        logger.info("[Breezy] Successfully connected to the page")

        return parseBreezyJobs(doc, target.name, target.url, target, logger)
    }

    private fun parseBreezyJobs(
        doc: Document,
        companyName: String,
        baseUrl: String,
        target: ScrapeTarget,
        logger: Logger,
    ): ScrapeOutput {
        val jobs = mutableListOf<Vacancy>()
        val jobCards = doc.select("li.position")
        logger.info("[Breezy] Found ${jobCards.size} job cards")

        jobCards.forEach { card ->
            try {
                val title = card.select("h2").text()
                val locationSpan =
                    card.select("li.location span")
                        .map { it.text().trim() }
                        .firstOrNull { it.isNotBlank() }
                val location =
                    when {
                        locationSpan == null -> ""
                        locationSpan.startsWith("%LABEL_") -> "Few places"
                        else -> locationSpan
                    }
                val department = card.select("li.department span").firstOrNull()?.text() ?: ""
                // Find the first a[href] inside li.position-actions (which may be a sibling)
                val positionActions = card.select("li.position-actions a[href]").firstOrNull()
                val relativeUrl = positionActions?.attr("href") ?: card.select("a").attr("href")
                val fullUrl = URI(baseUrl).resolve(relativeUrl).toString()

                jobs.add(
                    Vacancy(
                        title = title,
                        company = companyName,
                        location = location,
                        description = "Department: $department",
                        url = fullUrl,
                        source = "Breezy",
                    ),
                )
                logger.info("[Breezy] Successfully parsed job: $title at $companyName")
            } catch (e: Exception) {
                logger.error("[Breezy] Error parsing job card: ${e.message}", e)
            }
        }

        logger.info("[Breezy] Successfully parsed ${jobs.size} jobs")
        return ScrapeOutput(companyName, jobs, target)
    }
}
