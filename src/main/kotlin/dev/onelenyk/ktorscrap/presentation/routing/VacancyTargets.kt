package dev.onelenyk.ktorscrap.presentation.routing

import dev.onelenyk.ktorscrap.domain.model.ScrapeTarget
import dev.onelenyk.ktorscrap.domain.model.ScraperType

object VacancyTargets {
    val targets =
        listOf(
            ScrapeTarget(
                url = "https://djinni.co/jobs/?primary_keyword=Android&primary_keyword=Flutter",
                name = "Djinni",
                needsFiltering = false,
                scraperType = ScraperType.DJINNI,
            ),
            ScrapeTarget(
                url = "https://jobs.dou.ua/vacancies/?category=Android",
                name = "Dou|General",
                needsFiltering = true,
                scraperType = ScraperType.DOU,
            ),
            ScrapeTarget(
                url = "https://jobs.dou.ua/companies/promova/vacancies/",
                name = "Promova",
                needsFiltering = true,
                scraperType = ScraperType.DOU,
            ),
            ScrapeTarget(
                url = "https://jobs.dou.ua/companies/betterme/vacancies/",
                name = "BetterMe",
                needsFiltering = true,
                scraperType = ScraperType.DOU,
            ),
            ScrapeTarget(
                url = "https://jobs.dou.ua/companies/amo/vacancies/",
                name = "AMO",
                needsFiltering = true,
                scraperType = ScraperType.DOU,
            ),
            ScrapeTarget(
                url = "https://jobs.dou.ua/companies/boosta/vacancies/",
                name = "Boosta",
                needsFiltering = true,
                scraperType = ScraperType.DOU,
            ),
            ScrapeTarget(
                url = "https://jobs.dou.ua/companies/novapay/vacancies/",
                name = "NovaPay",
                needsFiltering = true,
                scraperType = ScraperType.DOU,
            ),
            ScrapeTarget(
                url = "https://jobs.lever.co/ajax",
                name = "Ajax Systems",
                needsFiltering = true,
                scraperType = ScraperType.LEVER,
            ),
            ScrapeTarget(
                url = "https://jobs.lever.co/kyivstar",
                name = "Kyivstar",
                needsFiltering = true,
                scraperType = ScraperType.LEVER,
            ),
            ScrapeTarget(
                url = "https://gen-tech.breezy.hr/?&department=Development#positions",
                name = "GenTech",
                needsFiltering = true,
                scraperType = ScraperType.BREEZY,
            ),
            ScrapeTarget(
                url = "https://skelar.breezy.hr/?&department=Mobile#positions",
                name = "Skelar",
                needsFiltering = true,
                scraperType = ScraperType.BREEZY,
            ),
            ScrapeTarget(
                url = "https://amo.tech/vacancies",
                name = "AMO",
                needsFiltering = true,
                scraperType = ScraperType.AMO,
            ),
        )
}
