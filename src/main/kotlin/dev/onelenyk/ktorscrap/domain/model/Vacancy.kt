package dev.onelenyk.ktorscrap.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Vacancy(
    val title: String,
    val company: String,
    val location: String,
    val salary: String? = null,
    val description: String,
    val url: String,
    val source: String,
)
