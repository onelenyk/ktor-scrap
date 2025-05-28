package dev.onelenyk.ktorscrap.app.di

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import dev.onelenyk.ktorscrap.app.env.DbCredentials
import dev.onelenyk.ktorscrap.app.env.EnvironmentManager
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
import dev.onelenyk.ktorscrap.features.vacanciesandroid.scraper.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import org.koin.dsl.module

val koinModule =
    module {
        // Environment and database setup
        single { EnvironmentManager.getDbCredentials() }
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
