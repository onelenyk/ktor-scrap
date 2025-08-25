package dev.onelenyk.ktorscrap.presentation.di

import dev.onelenyk.ktorscrap.data.db.Database
import dev.onelenyk.ktorscrap.data.db.FirestoreDatabase
import dev.onelenyk.ktorscrap.data.db.FirestoreService
import dev.onelenyk.ktorscrap.data.repository.FirestoreScrapingJobRepository
import dev.onelenyk.ktorscrap.data.repository.InMemoryScrapingJobRepository
import dev.onelenyk.ktorscrap.data.repository.ScrapingJobRepository
import dev.onelenyk.ktorscrap.data.repository.SwitchableScrapingJobRepository
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
import dev.onelenyk.ktorscrap.presentation.env.EnvironmentManager
import dev.onelenyk.ktorscrap.presentation.monitoring.SystemMonitor
import dev.onelenyk.ktorscrap.presentation.routing.DefaultJobsRoutes
import dev.onelenyk.ktorscrap.presentation.routing.ServerRouting
import dev.onelenyk.ktorscrap.presentation.routing.UtilRoutes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import org.koin.core.qualifier.named
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
        single {
            SystemHealthChecker(
                logger = get(),
                koin = getKoin(),
                firestore = get(),
            )
        }
        single { SystemMonitor(get(), get()) }

        // Services

        single<Database<ScraperTypeConfig>> {
            FirestoreService(
                get(),
                "scraper_types",
                ScraperTypeConfig::class.java,
            )
        }

        // Repository
        single<ScrapingJobRepository> {
            SwitchableScrapingJobRepository(
                firestoreRepository = get(named("firestore")),
                inMemoryRepository = get(named("inMemory")),
            )
        }
        single<ScrapingJobRepository>(named("firestore")) { FirestoreScrapingJobRepository(get()) }
        single<ScrapingJobRepository>(named("inMemory")) { InMemoryScrapingJobRepository() }

        // Job processing components
        single { JobOutputManager(get()) }
        single { JobProcessor(get(), get(), EnvironmentManager.getConcurrentJobLimit()) }
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
