package dev.onelenyk.ktorscrap.domain.model

import dev.onelenyk.ktorscrap.data.db.Identifiable
import kotlinx.serialization.Serializable

@Serializable
data class ScraperTypeConfig(
    override val id: String = "",
    val name: String = "",
    val enabled: Boolean = false,
) : Identifiable
