package dev.onelenyk.ktorscrap.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class ScraperType {
    AMO,
    BREEZY,
    DJINNI,
    DOU,
    LEVER,
    NOVADIGITAL,
    SAMPLE,
}
