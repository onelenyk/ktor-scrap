package dev.onelenyk.ktorscrap.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ScrapeTarget(
    val url: String,
    val name: String,
    val needsFiltering: Boolean,
    val scraperType: ScraperType,
)
