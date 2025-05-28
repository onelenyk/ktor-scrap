package dev.onelenyk.ktorscrap.features.vacanciesandroid.scraper

import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.*
import org.jsoup.Jsoup

class DouScraper : JobScraper {
    override suspend fun scrape(
        target: ScrapeTarget,
        logger: Logger,
    ): ScrapeOutput {
        logger.stage("DouScraper", "Start scraping for ${target.name} at ${target.url}")
        val jobs = mutableListOf<Vacancy>()
        try {
            val doc =
                Jsoup.connect(target.url)
                    .userAgent("Mozilla/5.0")
                    .get()
            val jobCards = doc.select("li.l-vacancy")
            logger.info("Found ${jobCards.size} job cards on page")

            jobCards.forEach { card ->
                try {
                    val title = card.select("a.vt").text()
                    val location = card.select("span.cities").text()
                    val url = card.select("a.vt").attr("href")
                    val company = card.select("a.company").text()
                    val description = card.select("div.sh-info").text()
                    jobs.add(
                        Vacancy(
                            title = title,
                            company = company,
                            location = location,
                            description = description,
                            url = url,
                            source = target.name,
                        ),
                    )
                    logger.info("Parsed job: $title at $company ($url)")
                } catch (e: Exception) {
                    logger.warn("Error parsing job card: ${e.message}")
                }
            }
        } catch (e: Exception) {
            logger.error("Error scraping DOU: ${e.message}", e)
        }
        logger.stage("DouScraper", "Completed scraping. Total jobs found: ${jobs.size}")
        return ScrapeOutput.withFiltering(
            source = target.name,
            vacancies = jobs,
            target = target,
        )
    }
}
