package de.dhbw.wikigame.api.wikimedia.interfaces

import androidx.room.Entity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.dhbw.wikigame.api.wikimedia.datatypes.WikimediaArticleStatistics
import de.dhbw.wikigame.api.wikimedia.datatypes.WikimediaData
import de.dhbw.wikigame.api.wikimedia.handlers.mostviewed.MostViewedArticlesAPIHandler
import kotlin.random.Random
import java.io.Serializable


class WikimediaStatsInterface(
    mostViewedArticlesJSONString: String,
    wikipediaArticlesShownJSONString: String
) {

    private var wikimediaDataInstance: WikimediaData? = null
    private var wikipediaArticlesShown: List<WikimediaArticleStatistics>? = null

    init {

        val moshiInstance: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val jsonAdapterWikimediaData: JsonAdapter<WikimediaData> =
            moshiInstance.adapter(WikimediaData::class.java)
        this.wikimediaDataInstance = jsonAdapterWikimediaData.fromJson(mostViewedArticlesJSONString)
        val parameterizedArticleList =
            Types.newParameterizedType(List::class.java, WikimediaArticleStatistics::class.java)
        /*val jsonAdapterArticlesShown: JsonAdapter<List<WikimediaArticleStatistics>> =
            moshiInstance.adapter(parameterizedArticleList)
        this.wikipediaArticlesShown =
            jsonAdapterArticlesShown.fromJson(wikipediaArticlesShownJSONString)*/

        this.wikipediaArticlesShown = listOf()

    }

    fun getRandomWikiArticle(): WikimediaArticleStatistics? {

        val wikipediaArticleList = wikimediaDataInstance!!.items!![0].articles ?: return null;

        var itemFound = false
        var randomArticleFound: WikimediaArticleStatistics? = null

        while (!itemFound) {
            val randomIndex = Random.nextInt(wikipediaArticleList!!.size)
            val currentRandomArticle = wikipediaArticleList[randomIndex]
            if (!wikipediaArticlesShown!!.contains(currentRandomArticle)) {
                itemFound = true
                randomArticleFound = currentRandomArticle
                wikipediaArticlesShown!!.plus(randomArticleFound)
            }
        }

        return randomArticleFound

    }

    fun getRandomWikiArticleUpperBound(): WikimediaArticleStatistics? {

        val wikipediaArticleList = wikimediaDataInstance!!.items!![0].articles ?: return null;

        var itemFound = false
        var randomArticleFound: WikimediaArticleStatistics? = null

        while (!itemFound) {
            val randomIndex = Random.nextInt(500, 999)
            val currentRandomArticle = wikipediaArticleList[randomIndex]
            if (!wikipediaArticlesShown!!.contains(currentRandomArticle)) {
                itemFound = true
                randomArticleFound = currentRandomArticle
                wikipediaArticlesShown!!.plus(randomArticleFound)
            }
        }

        return randomArticleFound

    }

    fun getRandomWikiArticleLowerBound(): WikimediaArticleStatistics? {

        val wikipediaArticleList = wikimediaDataInstance!!.items!![0].articles ?: return null;

        var itemFound = false
        var randomArticleFound: WikimediaArticleStatistics? = null

        while (!itemFound) {
            val randomIndex = Random.nextInt(0, 499)
            val currentRandomArticle = wikipediaArticleList[randomIndex]
            if (!wikipediaArticlesShown!!.contains(currentRandomArticle)) {
                itemFound = true
                randomArticleFound = currentRandomArticle
                wikipediaArticlesShown!!.plus(randomArticleFound)
            }
        }

        return randomArticleFound

    }


}