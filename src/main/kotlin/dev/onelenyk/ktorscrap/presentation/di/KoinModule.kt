package dev.onelenyk.ktorscrap.presentation.di

import com.google.cloud.firestore.Firestore
import dev.onelenyk.ktorscrap.data.db.Database
import dev.onelenyk.ktorscrap.data.db.FirestoreDatabase
import dev.onelenyk.ktorscrap.data.db.FirestoreService
import dev.onelenyk.ktorscrap.data.model.ScrapingJob
import dev.onelenyk.ktorscrap.data.repository.FirestoreScrapingJobRepository
import dev.onelenyk.ktorscrap.data.repository.ScraperTypeConfigRepository
import dev.onelenyk.ktorscrap.data.repository.ScrapingJobRepository
import dev.onelenyk.ktorscrap.data.scraper.*
import dev.onelenyk.ktorscrap.domain.model.ConsoleLogger
import dev.onelenyk.ktorscrap.domain.model.Logger
import dev.onelenyk.ktorscrap.domain.model.ScraperTypeConfig
import dev.onelenyk.ktorscrap.domain.usecase.JobOutputManager
import dev.onelenyk.ktorscrap.domain.usecase.JobProcessor
import dev.onelenyk.ktorscrap.domain.usecase.JobQueueManager
import dev.onelenyk.ktorscrap.presentation.env.EnvironmentManager
import dev.onelenyk.ktorscrap.presentation.routing.ServerRouting
import dev.onelenyk.ktorscrap.presentation.routing.UtilRoutes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import org.koin.dsl.module

val koinModule =
    module {
        // Environment and database setup
        single { EnvironmentManager.getDbCredentials() }
        single { FirestoreDatabase(get()) }
        single { get<FirestoreDatabase>().db }

        // Core components
        single { provideCoroutineScope() }
        single<Logger> { ConsoleLogger(enabled = true) }

        // Services
        single<Database<ScrapingJob>> {
            FirestoreService(
                get(),
                "scraping_jobs",
                ScrapingJob::class.java,
            )
        }
        single<Database<ScraperTypeConfig>> {
            FirestoreService(
                get(),
                "scraper_types",
                ScraperTypeConfig::class.java,
            )
        }

        // Repository
        single<ScrapingJobRepository> { FirestoreScrapingJobRepository(get()) }
        single { ScraperTypeConfigRepository(get()) }

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
        single { ServerRouting(get(), get(), get()) }
        single { UtilRoutes() }
    }

@OptIn(DelicateCoroutinesApi::class)
private fun provideCoroutineScope(): CoroutineScope {
    return GlobalScope
}
