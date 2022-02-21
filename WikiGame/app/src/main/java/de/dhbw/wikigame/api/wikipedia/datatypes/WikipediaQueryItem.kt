package de.dhbw.wikigame.api.wikipedia.datatypes

class WikipediaQueryItem(
    @Transient val normalized: String? = null,
    val pages: Array<WikipediaPagesItem>) {
}