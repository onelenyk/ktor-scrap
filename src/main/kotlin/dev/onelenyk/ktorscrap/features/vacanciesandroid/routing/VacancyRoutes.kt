package dev.onelenyk.ktorscrap.features.vacanciesandroid.routing

import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.*
import dev.onelenyk.ktorscrap.features.vacanciesandroid.scraper.*
import io.ktor.http.ContentType
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

private val jobSources = listOf<JobSource>()
/*    listOf(
        JobSource(
            DjinniScraper(),
            ScrapeTarget(
                url = "https://djinni.co/jobs/?primary_keyword=Android&primary_keyword=Flutter",
                name = "Djinni",
                needsFiltering = false,
            ),
        ),
        JobSource(
            DouScraper(),
            ScrapeTarget(
                url = "https://jobs.dou.ua/vacancies/?category=Android",
                name = "Dou|General",
                needsFiltering = true,
            ),
        ),
        JobSource(
            DouScraper(),
            ScrapeTarget(
                url = "https://jobs.dou.ua/companies/promova/vacancies/",
                name = "Promova",
                needsFiltering = true,
            ),
        ),
        JobSource(
            DouScraper(),
            ScrapeTarget(
                url = "https://jobs.dou.ua/companies/betterme/vacancies/",
                name = "BetterMe",
                needsFiltering = true,
            ),
        ),
        JobSource(
            DouScraper(),
            ScrapeTarget(
                url = "https://jobs.dou.ua/companies/amo/vacancies/",
                name = "AMO",
                needsFiltering = true,
            ),
        ),
        JobSource(
            DouScraper(),
            ScrapeTarget(
                url = "https://jobs.dou.ua/companies/boosta/vacancies/",
                name = "Boosta",
                needsFiltering = true,
            ),
        ),
        JobSource(
            DouScraper(),
            ScrapeTarget(
                url = "https://jobs.dou.ua/companies/novapay/vacancies/",
                name = "NovaPay",
                needsFiltering = true,
            ),
        ),
        JobSource(
            LeverScraper(),
            ScrapeTarget(
                url = "https://jobs.lever.co/ajax",
                name = "Ajax Systems",
                needsFiltering = true,
            ),
        ),
        JobSource(
            LeverScraper(),
            ScrapeTarget(
                url = "https://jobs.lever.co/kyivstar",
                name = "Kyivstar",
                needsFiltering = true,
            ),
        ),
        JobSource(
            BreezyScraper(),
            ScrapeTarget(
                url = "https://gen-tech.breezy.hr/?&department=Development#positions",
                name = "GenTech",
                needsFiltering = true,
            ),
        ),
        JobSource(
            BreezyScraper(),
            ScrapeTarget(
                url = "https://skelar.breezy.hr/?&department=Mobile#positions",
                name = "Skelar",
                needsFiltering = true,
            ),
        ),
        JobSource(
            AmoScraper(),
            ScrapeTarget(
                url = "https://amo.tech/vacancies",
                name = "AMO",
                needsFiltering = true,
            ),
        ),
    )*/
