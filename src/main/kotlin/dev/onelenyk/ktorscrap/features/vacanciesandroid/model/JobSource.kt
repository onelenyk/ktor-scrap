package dev.onelenyk.ktorscrap.features.vacanciesandroid.model

import dev.onelenyk.ktorscrap.features.vacanciesandroid.scraper.JobScraper

data class JobSource(
    val scraper: JobScraper,
    val target: ScrapeTarget,
)
