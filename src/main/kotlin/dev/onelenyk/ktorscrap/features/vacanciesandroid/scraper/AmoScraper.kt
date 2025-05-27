package dev.onelenyk.ktorscrap.features.vacanciesandroid.scraper

import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.Logger
import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.ScrapeOutput
import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.ScrapeTarget
import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.Vacancy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class AmoScraper : JobScraper {
    override suspend fun scrape(
        target: ScrapeTarget,
        logger: Logger,
    ): ScrapeOutput {
        logger.info("[AMO] Starting to scrape: ${target.url}")
        val doc =
            withContext(Dispatchers.IO) {
                Jsoup.connect(target.url)
                    .userAgent("Mozilla/5.0")
                    .get()
            }
        logger.info("[AMO] Successfully connected to the page")
        return parseAmoJobs(doc, target.name, target, logger)
    }

    private fun parseAmoJobs(
        doc: Document,
        companyName: String,
        target: ScrapeTarget,
        logger: Logger,
    ): ScrapeOutput {
        val jobs = mutableListOf<Vacancy>()
        val wrapper = doc.selectFirst("div.vacancies-wrapper")
        if (wrapper == null) {
            logger.error("[AMO] No vacancies-wrapper found", null)
            return ScrapeOutput(companyName, jobs, target)
        }
        val departmentBlocks = wrapper.select("div.wrapper")
        logger.info("[AMO] Found ${departmentBlocks.size} department blocks")
        departmentBlocks.forEach { block ->
            val department = block.selectFirst("div.name")?.text()?.trim() ?: ""
            val vacancies = block.select("div.columns.is-multiline.vacancies > div")
            logger.info("[AMO] Department '$department' has ${vacancies.size} vacancies")
            vacancies.forEach { vacancy ->
                try {
                    val title = vacancy.selectFirst(".title, h3, h2")?.text()?.trim() ?: ""
                    val location = vacancy.selectFirst(".location")?.text()?.trim() ?: ""
                    val url = vacancy.selectFirst("a[href]")?.absUrl("href") ?: ""
                    jobs.add(
                        Vacancy(
                            title = title,
                            company = companyName,
                            location = location,
                            description = if (department.isNotBlank()) "Department: $department" else "",
                            url = url,
                            source = "AMO",
                        ),
                    )
                    logger.info("[AMO] Parsed job: $title in $department")
                } catch (e: Exception) {
                    logger.error("[AMO] Error parsing vacancy: ${e.message}", e)
                }
            }
        }
        logger.info("[AMO] Successfully parsed ${jobs.size} jobs")
        return ScrapeOutput(companyName, jobs, target)
    }
}
