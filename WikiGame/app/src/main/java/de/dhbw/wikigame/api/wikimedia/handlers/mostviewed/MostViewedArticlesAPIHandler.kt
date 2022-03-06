package de.dhbw.wikigame.api.wikimedia.handlers.mostviewed

import android.content.SharedPreferences
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.dhbw.wikigame.api.wikimedia.datatypes.WikimediaData
import de.dhbw.wikigame.api.wikimedia.interfaces.WikimediaStatsInterface
import de.dhbw.wikigame.util.WikimediaDateUtil
import de.dhbw.wikigame.util.WikipediaLanguage
import okhttp3.*
import java.io.IOException
import java.io.Serializable

class MostViewedArticlesAPIHandler(val currentWikiLanguage: WikipediaLanguage) {

    private val httpClient = OkHttpClient()
    private var mostViewedArticlesJSONString: String? = null

    init {
        getMostViewedArticles()
    }

    private fun getMostViewedArticlesRequestURL(): String {

        val getRequestDatePattern = WikimediaDateUtil().getGETRequestFormattedDatePattern()
        val wikimediaBaseURL =
            "https://wikimedia.org/api/rest_v1/metrics/pageviews/top/${(currentWikiLanguage.toString().lowercase())}.wikipedia/all-access/"
        return wikimediaBaseURL + getRequestDatePattern

    }

    fun getMostViewedArticlesJSONString(): String {
        return mostViewedArticlesJSONString!!
    }

    private fun getMostViewedArticles() {

        val requestURL = getMostViewedArticlesRequestURL()
        val articleRequest = Request.Builder()
            .url(requestURL)
            .header("Accept", "application/json").build()

        httpClient.newCall(articleRequest).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {

                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val responseBody: ResponseBody = response.body!!
                    val responseBodyContent: String = responseBody.string()
                    mostViewedArticlesJSONString = responseBodyContent

                }
            }

        })

    }

}