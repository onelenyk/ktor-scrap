package dev.onelenyk.ktorscrap.features.vacanciesandroid.scraper

import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.Logger
import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.ScrapeOutput
import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.ScrapeTarget

class ScraperManager(
    private val djinniScraper: DjinniScraper,
    private val douScraper: DouScraper,
    private val amoScraper: AmoScraper,
    private val novaDigitalScraper: NovaDigitalScraper,
    private val breezyScraper: BreezyScraper,
    private val leverScraper: LeverScraper,
    private val logger: Logger,
) {
    suspend fun scrape(target: ScrapeTarget): ScrapeOutput {
        val scraper = selectScraper(target.url)
        logger.info("Selected scraper: ${scraper.javaClass.simpleName} for URL: ${target.url}")
        return scraper.scrape(target, logger)
    }

    private fun selectScraper(url: String): JobScraper {
        return when {
            url.contains("djinni.co") -> djinniScraper
            url.contains("jobs.dou.ua") -> douScraper
            url.contains("amo.ua") -> amoScraper
            url.contains("novadigital") -> novaDigitalScraper
            url.contains("breezy.hr") -> breezyScraper
            url.contains("lever.co") -> leverScraper
            else -> throw IllegalArgumentException("No suitable scraper found for URL: $url")
        }
    }
} 
