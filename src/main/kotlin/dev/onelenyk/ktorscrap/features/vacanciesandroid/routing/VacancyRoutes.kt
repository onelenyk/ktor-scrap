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

fun Routing.vacancyRoutes() {
    get("/vacancies/json") {
        try {
            val result = withTimeout(600_000) { withContext(Dispatchers.Default) { runScrapingCollect() } }
            call.respond(result)
        } catch (e: Exception) {
            call.respond(mapOf("error" to (e.message ?: "Unknown error")))
        }
    }

    get("/vacancies/html") {
        try {
            val result = withTimeout(600_000) { withContext(Dispatchers.Default) { runScrapingCollect() } }
            val html =
                buildString {
                    append("<html><head><title>Vacancies</title></head><body>")
                    append("<h1>Vacancies</h1>")
                    append("<div style='margin-bottom:2em;padding:1em;border:1px solid #aaa;background:#f9f9f9;'>")
                    append("<b>Processed Sites:</b><ul>")
                    jobSources.forEach { source ->
                        append("<li><a href='${source.target.url}' target='_blank'>${source.target.name}</a></li>")
                    }
                    append("</ul></div>")
                    result.forEach { vacancy ->
                        append("<div style='margin-bottom:2em;padding:1em;border:1px solid #ccc;'>")
                        append("<b>Title:</b> ${vacancy.title}<br>")
                        append("<b>Company:</b> ${vacancy.company}<br>")
                        append("<b>Location:</b> ${vacancy.location}<br>")
                        vacancy.salary?.let { append("<b>Salary:</b> $it<br>") }
                        append("<b>Description:</b> ${vacancy.description}<br>")
                        append("<b>Source:</b> ${vacancy.source}<br>")
                        append("<b>URL:</b> <a href='${vacancy.url}'>${vacancy.url}</a><br>")
                        append("</div>")
                    }
                    append("</body></html>")
                }
            call.respondText(html, contentType = ContentType.Text.Html)
        } catch (e: Exception) {
            val html =
                """
                <html><head><title>Error</title></head><body>
                <h1>Error</h1>
                <p>${e.message ?: "Unknown error"}</p>
                </body></html>
                """.trimIndent()
            call.respondText(html, contentType = ContentType.Text.Html)
        }
    }
}

suspend fun runScrapingCollect(): List<Vacancy> =
    withContext(Dispatchers.Default) {
        val logger = ConsoleLogger(enabled = true)
        val jobs =
            jobSources.map { source ->
                async {
                    try {
                        val vacancyList = source.scraper.scrape(source.target, logger)
                        val allVacancies = vacancyList.vacancies
                        if (source.target.needsFiltering) {
                            allVacancies.filter { VacancyFilter.isRelevant(it) }
                        } else {
                            allVacancies
                        }
                    } catch (e: Exception) {
                        emptyList<Vacancy>()
                    }
                }
            }
        jobs.awaitAll().flatten()
    }
