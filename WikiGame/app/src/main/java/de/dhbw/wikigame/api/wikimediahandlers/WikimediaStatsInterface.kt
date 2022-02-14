package de.dhbw.wikigame.api.wikimediahandlers

import de.dhbw.wikigame.api.wikimediadatatypes.WikimediaArticleStatistics

class WikimediaStatsInterface {

    public fun getTopArticlesYesterday(): List<WikimediaArticleStatistics> {


        val testObject = WikimediaArticleStatistics("ZTest", 1, 1)
        return listOf(testObject)

    }

}