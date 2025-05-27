package dev.onelenyk.ktorscrap.features.vacanciesandroid.scraper

import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.Logger
import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.ScrapeTarget
import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.ScrapeOutput

interface JobScraper {
    suspend fun scrape(
        target: ScrapeTarget,
        logger: Logger,
    ): ScrapeOutput
}
