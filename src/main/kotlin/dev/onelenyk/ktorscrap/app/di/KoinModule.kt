package dev.onelenyk.ktorscrap.app.di

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import dev.onelenyk.ktorscrap.app.routing.ServerRouting
import dev.onelenyk.ktorscrap.app.routing.UtilRoutes
import dev.onelenyk.ktorscrap.data.db.MongoDBManager
import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.ConsoleLogger
import dev.onelenyk.ktorscrap.features.vacanciesandroid.model.Logger
import dev.onelenyk.ktorscrap.features.vacanciesandroid.processor.JobOutputManager
import dev.onelenyk.ktorscrap.features.vacanciesandroid.processor.JobProcessor
import dev.onelenyk.ktorscrap.features.vacanciesandroid.processor.JobQueueManager
import dev.onelenyk.ktorscrap.features.vacanciesandroid.repository.ScrapingJobRepository
import dev.onelenyk.ktorscrap.features.vacanciesandroid.repository.ScrapingJobRepositoryImpl
import dev.onelenyk.ktorscrap.features.vacanciesandroid.repository.model.ScrapingJob
import dev.onelenyk.ktorscrap.features.vacanciesandroid.scraper.AmoScraper
import dev.onelenyk.ktorscrap.features.vacanciesandroid.scraper.BreezyScraper
import dev.onelenyk.ktorscrap.features.vacanciesandroid.scraper.DjinniScraper
import dev.onelenyk.ktorscrap.features.vacanciesandroid.scraper.DouScraper
import dev.onelenyk.ktorscrap.features.vacanciesandroid.scraper.LeverScraper
import dev.onelenyk.ktorscrap.features.vacanciesandroid.scraper.NovaDigitalScraper
import dev.onelenyk.ktorscrap.features.vacanciesandroid.scraper.ScraperManager
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import org.koin.dsl.module

val koinModule =
    module {
        // Environment and database setup
        single { dotenv() }
        single { provideDbCredentials(get()) }
        single { MongoDBManager(get()) }
        single { database(get()) }

        // Core components
        single { provideCoroutineScope() }
        single<Logger> { ConsoleLogger(enabled = true) }

        // Repository
        single<ScrapingJobRepository> { ScrapingJobRepositoryImpl(get()) }

        // Job processing components
        single { JobOutputManager(get()) }
        single { JobProcessor(get(), get()) }
        single { JobQueueManager(get(), get(), get(), get()) }

        // Scrapers
        single { DjinniScraper() }
        single { DouScraper() }
        single { AmoScraper() }
        single { NovaDigitalScraper() }
        single { BreezyScraper() }
        single { LeverScraper() }
        single { ScraperManager(get(), get(), get(), get(), get(), get(), get()) }

        // Routing
        single { ServerRouting(get(), get()) }
        single { UtilRoutes() }
    }

@OptIn(DelicateCoroutinesApi::class)
private fun provideCoroutineScope(): CoroutineScope {
    return GlobalScope
}

fun database(mongoDBManager: MongoDBManager): MongoDatabase {
    return mongoDBManager.getDatabase()
}

fun provideDbCredentials(dotenv: Dotenv): DbCredentials {
    val username = dotenv["DB_USERNAME"]
    val pass = dotenv["DB_PASSWORD"]
    val connection = dotenv["DB_CONNECTION"]
    return DbCredentials(username, pass, connection)
}

fun provideServerPort(dotenv: Dotenv): Int {
    val port = dotenv["PORT"].toIntOrNull()
    return port ?: 8080
}

data class DbCredentials(val username: String, val password: String, val connection: String)
