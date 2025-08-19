package dev.onelenyk.ktorscrap.data.scraper

import dev.onelenyk.ktorscrap.domain.model.Logger
import dev.onelenyk.ktorscrap.domain.model.ScrapeOutput
import dev.onelenyk.ktorscrap.domain.model.ScrapeTarget
import dev.onelenyk.ktorscrap.domain.model.ScraperType
import dev.onelenyk.ktorscrap.domain.repository.JobScraper

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
        val scraper = selectScraper(target.scraperType)
        logger.info("Selected scraper: ${scraper.javaClass.simpleName} for URL: ${target.url}")
        return scraper.scrape(target, logger)
    }

    private fun selectScraper(scraperType: ScraperType): JobScraper {
        return when (scraperType) {
            ScraperType.AMO -> amoScraper
            ScraperType.BREEZY -> breezyScraper
            ScraperType.DJINNI -> djinniScraper
            ScraperType.DOU -> douScraper
            ScraperType.LEVER -> leverScraper
            ScraperType.NOVADIGITAL -> novaDigitalScraper
            ScraperType.SAMPLE -> throw IllegalArgumentException("Sample scraper is not meant for direct use.")
        }
    }
}
