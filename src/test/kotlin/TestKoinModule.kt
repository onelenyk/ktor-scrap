package dev.onelenyk.ktorscrap.test

import dev.onelenyk.ktorscrap.data.db.Database
import dev.onelenyk.ktorscrap.data.db.FirestoreDatabase
import dev.onelenyk.ktorscrap.data.db.FirestoreService
import dev.onelenyk.ktorscrap.data.repository.InMemoryScrapingJobRepository
import dev.onelenyk.ktorscrap.data.repository.ScrapingJobRepository
import dev.onelenyk.ktorscrap.data.scraper.AmoScraper
import dev.onelenyk.ktorscrap.data.scraper.BreezyScraper
import dev.onelenyk.ktorscrap.data.scraper.DjinniScraper
import dev.onelenyk.ktorscrap.data.scraper.DouScraper
import dev.onelenyk.ktorscrap.data.scraper.LeverScraper
import dev.onelenyk.ktorscrap.data.scraper.NovaDigitalScraper
import dev.onelenyk.ktorscrap.data.scraper.ScraperManager
import dev.onelenyk.ktorscrap.domain.model.ConsoleLogger
import dev.onelenyk.ktorscrap.domain.model.Logger
import dev.onelenyk.ktorscrap.domain.model.ScraperTypeConfig
import dev.onelenyk.ktorscrap.domain.usecase.JobOutputManager
import dev.onelenyk.ktorscrap.domain.usecase.JobProcessor
import dev.onelenyk.ktorscrap.domain.usecase.JobQueueManager
import dev.onelenyk.ktorscrap.presentation.di.SystemHealthChecker
import dev.onelenyk.ktorscrap.presentation.env.EnvironmentManager
import dev.onelenyk.ktorscrap.presentation.monitoring.SystemMonitor
import dev.onelenyk.ktorscrap.presentation.routing.DefaultJobsRoutes
import dev.onelenyk.ktorscrap.presentation.routing.ServerRouting
import dev.onelenyk.ktorscrap.presentation.routing.UtilRoutes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import org.koin.dsl.module

val testKoinModule =
    module {
        // Mock Firestore for tests
        single { FirestoreDatabase(get()) }
        single { get<FirestoreDatabase>().db }

        // Environment and database setup
        single { EnvironmentManager.getDbCredentials() }
        // Use mock Firestore directly, no need for FirestoreDatabase in tests

        // Core components
        single { provideCoroutineScope() }
        single<Logger> { ConsoleLogger(enabled = true) }
        single {
            SystemHealthChecker(
                logger = get(),
                koin = getKoin(),
                // This will be the mocked Firestore
                firestore = get(),
            )
        }
        single { SystemMonitor(get(), get()) }

        // Services
        single<Database<ScraperTypeConfig>> {
            FirestoreService<ScraperTypeConfig>(
                get(),
                "scraper_types",
                ScraperTypeConfig::class.java,
            )
        }

        // Repository
        single<ScrapingJobRepository> { InMemoryScrapingJobRepository() }

        // Job processing components
        single { JobOutputManager(get()) }
        single { JobProcessor(get(), get(), 5) }
        single { JobQueueManager(get(), get(), get()) }

        // Scrapers
        single { DjinniScraper() }
        single { DouScraper() }
        single { AmoScraper() }
        single { NovaDigitalScraper() }
        single { BreezyScraper() }
        single { LeverScraper() }
        single { ScraperManager(get(), get(), get(), get(), get(), get(), get()) }

        // Routing
        single { UtilRoutes(get()) }
        single { DefaultJobsRoutes(get()) }
        single { ServerRouting(get(), get(), get(), get()) }
    }

@OptIn(DelicateCoroutinesApi::class)
private fun provideCoroutineScope(): CoroutineScope {
    return GlobalScope
}
