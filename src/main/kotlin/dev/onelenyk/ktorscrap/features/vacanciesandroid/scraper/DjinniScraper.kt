package dev.onelenyk.ktorscrap.features.vacanciesandroid.scraper

import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.*
import org.jsoup.Jsoup

class DjinniScraper : JobScraper {
    private val pageLimit = 1

    override suspend fun scrape(
        target: ScrapeTarget,
        logger: Logger,
    ): ScrapeOutput {
        val jobs = mutableListOf<Vacancy>()
        var currentPage = 1
        var hasNextPage = true
        logger.stage("DjinniScraper", "Start scraping for ${target.name} at ${target.url}")

        while (hasNextPage && currentPage <= pageLimit) {
            try {
                val pageUrl =
                    if (currentPage == 1) {
                        target.url
                    } else {
                        target.url.replace("page=\\d+".toRegex(), "page=$currentPage")
                            .let { if (it.contains("page=")) it else "$it&page=$currentPage" }
                    }
                logger.stage("DjinniScraper", "Fetching page $currentPage: $pageUrl")
                val doc =
                    Jsoup.connect(target.url)
                        .userAgent("Mozilla/5.0")
                        .get()
                val jobCards = doc.select("ul.list-unstyled.list-jobs.mb-4 > li.mb-4")
                logger.info("Found ${jobCards.size} job cards on page $currentPage")

                jobCards.forEach { card ->
                    try {
                        val title = card.select("div.d-flex.flex-wrap.align-items-center.gap-1.fs-5.mb-2.text-secondary").text()
                        val company = card.select("div.d-inline-flex.align-items-center.gap-1").text()
                        val location = card.select("div.d-flex.flex-wrap.align-items-center.gap-1").text()
                        val url = card.select("a.job-item__title-link").attr("href")
                        val fullUrl = if (url.startsWith("http")) url else "https://djinni.co$url"
                        val description = card.select("div.list-jobs__description").text()
                        val salary = card.select("div.public-salary-item").text()
                        jobs.add(
                            Vacancy(
                                title = title,
                                company = company,
                                location = location,
                                salary = salary.takeIf { it.isNotEmpty() },
                                description = description,
                                url = fullUrl,
                                source = target.name,
                            ),
                        )
                        logger.info("Parsed job: $title at $company (URL: $fullUrl)")
                    } catch (e: Exception) {
                        logger.warn("Error parsing job card: ${e.message}")
                    }
                }

                hasNextPage = jobCards.isNotEmpty()
                currentPage++
            } catch (e: Exception) {
                logger.error("Error scraping page $currentPage: ${e.message}", e)
                hasNextPage = false
            }
        }
        logger.stage("DjinniScraper", "Completed scraping. Total jobs found: ${jobs.size}")
        return ScrapeOutput(
            source = target.name,
            vacancies = jobs,
            target = target,
        )
    }
}
