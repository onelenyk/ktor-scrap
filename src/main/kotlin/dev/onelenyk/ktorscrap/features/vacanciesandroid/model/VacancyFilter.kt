package dev.onelenyk.ktorscrap.features.vacanciesandroid.model

object VacancyFilter {
    private val relevantKeywords =
        listOf(
            "mobile",
            "android",
            "ios",
            "flutter",
            "kotlin",
            "swift",
        )

    fun isRelevant(vacancy: Vacancy): Boolean {
        val searchText = "${vacancy.title} ${vacancy.description} ${vacancy.company}".lowercase()
        return relevantKeywords.any { keyword -> searchText.contains(keyword.lowercase()) }
    }
}
