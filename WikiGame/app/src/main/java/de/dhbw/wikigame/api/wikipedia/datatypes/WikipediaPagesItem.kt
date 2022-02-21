package de.dhbw.wikigame.api.wikipedia.datatypes

class WikipediaPagesItem(
    val pageid: String,
    val ns: String,
    val title: String,
    val thumbnail: WikipediaThumbnail,
    val pageimage: String,
    @Transient val terms: String? = null
) {
}