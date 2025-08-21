package dev.onelenyk.ktorscrap.data.repository

import dev.onelenyk.ktorscrap.data.db.Database
import dev.onelenyk.ktorscrap.domain.model.ScraperTypeConfig

class ScraperTypeConfigRepository(private val firestoreService: Database<ScraperTypeConfig>) {
    suspend fun getAll(): List<ScraperTypeConfig> = firestoreService.getAll()

    suspend fun getById(id: String): ScraperTypeConfig? = firestoreService.getById(id)

    suspend fun create(scraperTypeConfig: ScraperTypeConfig): ScraperTypeConfig = firestoreService.create(scraperTypeConfig)

    suspend fun update(scraperTypeConfig: ScraperTypeConfig): ScraperTypeConfig = firestoreService.update(scraperTypeConfig)

    suspend fun delete(id: String) = firestoreService.delete(id)
}
