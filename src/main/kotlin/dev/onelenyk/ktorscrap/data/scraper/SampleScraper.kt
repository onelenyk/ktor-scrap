package dev.onelenyk.ktorscrap.data.scraper

import dev.onelenyk.ktorscrap.domain.model.*
import dev.onelenyk.ktorscrap.domain.repository.JobScraper

class SampleScraper : JobScraper {
    override suspend fun scrape(
        target: ScrapeTarget,
        logger: Logger,
    ): ScrapeOutput {
        logger.stage("SampleScraper", "Scraping fake data for ${target.name} at ${target.url}")
        val vacancies =
            listOf(
                Vacancy(
                    title = "Android Developer",
                    company = "FakeCompany",
                    location = "Remote",
                    salary = "3000-4000 USD",
                    description = "Work on a fake Android app.",
                    url = target.url + "/vacancy/1",
                    source = target.name,
                ),
                Vacancy(
                    title = "iOS Developer",
                    company = "FakeCompany",
                    location = "Remote",
                    salary = "3500-4500 USD",
                    description = "Work on a fake iOS app.",
                    url = target.url + "/vacancy/2",
                    source = target.name,
                ),
            )
        logger.info("SampleScraper produced ${vacancies.size} vacancies.")
        return ScrapeOutput.withFiltering(
            source = target.name,
            vacancies = vacancies,
            target = target,
        )
    }
}
