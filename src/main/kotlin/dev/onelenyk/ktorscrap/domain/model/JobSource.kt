package dev.onelenyk.ktorscrap.domain.model

import dev.onelenyk.ktorscrap.domain.repository.JobScraper

data class JobSource(
    val scraper: JobScraper,
    val target: ScrapeTarget,
)
