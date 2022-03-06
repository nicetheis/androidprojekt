package de.dhbw.wikigame.api.wikimedia.interfaces

import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.dhbw.wikigame.api.wikimedia.datatypes.WikimediaArticleStatistics
import de.dhbw.wikigame.api.wikimedia.datatypes.WikimediaData
import de.dhbw.wikigame.api.wikimedia.handlers.mostviewed.MostViewedArticlesAPIHandler
import kotlin.random.Random


class WikimediaStatsInterface {

    private var wikimediaDataInstanceMap: HashMap<String, WikimediaData>? = null

    init {

        wikimediaDataInstanceMap = hashMapOf<String, WikimediaData>()

        val moshiInstance: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val jsonAdapterWikimediaData: JsonAdapter<WikimediaData> =
            moshiInstance.adapter(WikimediaData::class.java)
        // DE
        val jsonStringDE = MostViewedArticlesJSONStorage.mostViewedArticlesJSONStrings.get("de")
        if(jsonStringDE == null) Log.d("DEBUG", "jsonStringDE IS NULL")
        this.wikimediaDataInstanceMap!!["de"] =
            jsonAdapterWikimediaData.fromJson(jsonStringDE)!!
        // FR
        val jsonStringFR = MostViewedArticlesJSONStorage.mostViewedArticlesJSONStrings.get("fr")
        if(jsonStringFR == null) Log.d("DEBUG", "jsonStringFR IS NULL")
        this.wikimediaDataInstanceMap!!["fr"] =
            jsonAdapterWikimediaData.fromJson(jsonStringFR)!!
        // EN
        val jsonStringEN = MostViewedArticlesJSONStorage.mostViewedArticlesJSONStrings.get("en")
        if(jsonStringEN == null) Log.d("DEBUG", "jsonStringEN IS NULL")
        this.wikimediaDataInstanceMap!!["en"] =
            jsonAdapterWikimediaData.fromJson(jsonStringEN)!!

    }

    private fun formatToMostViewedJSONString(currentWikiLanguage: String): String {
        val moshiInstance: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val jsonAdapterWikimediaData: JsonAdapter<WikimediaData> =
            moshiInstance.adapter(WikimediaData::class.java)
        return jsonAdapterWikimediaData.toJson(wikimediaDataInstanceMap!![currentWikiLanguage]!!)

    }

    fun getRandomWikiArticle(currentWikiLanguage: String): WikimediaArticleStatistics? {

        val wikipediaArticleList =
            wikimediaDataInstanceMap!![currentWikiLanguage]!!.items!![0].articles ?: return null
        var mutableArticleList: MutableList<WikimediaArticleStatistics> =
            wikipediaArticleList.toMutableList()
        println("mutableArticleList size before removal: ${mutableArticleList.size}")
        mutableArticleList = mutableArticleList.drop(2).toMutableList()
        println("mutableArticleList size after removal: ${mutableArticleList.size}")

        var itemFound = false
        var randomArticleFound: WikimediaArticleStatistics? = null

        while (!itemFound) {
            val randomIndex = Random.nextInt(mutableArticleList!!.size)
            val currentRandomArticle = mutableArticleList[randomIndex]
            itemFound = true
            randomArticleFound = currentRandomArticle
            mutableArticleList.remove(currentRandomArticle)
            wikimediaDataInstanceMap!![currentWikiLanguage]!!.items!![0].articles =
                mutableArticleList.toTypedArray()
            MostViewedArticlesJSONStorage.mostViewedArticlesJSONStrings.set(
                currentWikiLanguage,
                formatToMostViewedJSONString(currentWikiLanguage)
            )
        }

        return randomArticleFound

    }

    fun getRandomWikiArticle(
        currentWikiLanguage: String,
        articleBound: WikimediaArticleBound
    ): WikimediaArticleStatistics? {

        val wikipediaArticleList =
            wikimediaDataInstanceMap!![currentWikiLanguage]!!.items!![0].articles ?: return null
        var mutableArticleList: MutableList<WikimediaArticleStatistics> =
            wikipediaArticleList.toMutableList()
        println("mutableArticleList size before removal: ${mutableArticleList.size}")
        mutableArticleList = mutableArticleList.drop(2).toMutableList()
        println("mutableArticleList size after removal: ${mutableArticleList.size}")

        var itemFound = false
        var randomArticleFound: WikimediaArticleStatistics? = null

        while (!itemFound) {
            var randomIndex = 0
            var arraySize = mutableArticleList.size
            var arrayHalf = Math.round((arraySize/2).toDouble()).toInt()
            if (articleBound == WikimediaArticleBound.UPPER) {
                randomIndex = Random.nextInt(arrayHalf, arraySize)
            } else if (articleBound == WikimediaArticleBound.LOWER) {
                randomIndex = Random.nextInt(0, arrayHalf-1)
            }
            val currentRandomArticle = mutableArticleList[randomIndex]
            itemFound = true
            randomArticleFound = currentRandomArticle
            mutableArticleList.remove(currentRandomArticle)
            wikimediaDataInstanceMap!![currentWikiLanguage]!!.items!![0].articles =
                mutableArticleList.toTypedArray()
            MostViewedArticlesJSONStorage.mostViewedArticlesJSONStrings.set(
                currentWikiLanguage,
                formatToMostViewedJSONString(currentWikiLanguage)
            )
        }

        return randomArticleFound

    }

}