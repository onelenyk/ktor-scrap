package dev.onelenyk.ktorscrap.domain.repository

import dev.onelenyk.ktorscrap.domain.model.Logger
import dev.onelenyk.ktorscrap.domain.model.ScrapeOutput
import dev.onelenyk.ktorscrap.domain.model.ScrapeTarget

interface JobScraper {
    suspend fun scrape(
        target: ScrapeTarget,
        logger: Logger,
    ): ScrapeOutput
}
