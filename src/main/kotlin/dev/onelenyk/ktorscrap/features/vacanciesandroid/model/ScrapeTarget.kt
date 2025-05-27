package dev.onelenyk.ktorscrap.features.vacanciesandroid.model

import kotlinx.serialization.Serializable

@Serializable
data class ScrapeTarget(
    val url: String,
    val name: String,
    val needsFiltering: Boolean,
)
