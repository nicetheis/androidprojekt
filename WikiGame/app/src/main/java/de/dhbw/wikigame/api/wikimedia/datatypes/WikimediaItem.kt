package de.dhbw.wikigame.api.wikimedia.datatypes

class WikimediaItem {

    var project: String? = null
    var access: String? = null
    var year: String? = null
    var month: String? = null
    var day: String? = null
    var articles: Array<WikimediaArticleStatistics>? = null
}