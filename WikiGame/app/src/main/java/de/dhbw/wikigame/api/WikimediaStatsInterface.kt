package de.dhbw.wikigame.api

class WikimediaStatsInterface {

    public fun getTopArticlesYesterday(): List<WikipediaArticleStatsEntry> {


        val testObject = WikipediaArticleStatsEntry("ZTest", 1, 1)
        return listOf(testObject)

    }

}