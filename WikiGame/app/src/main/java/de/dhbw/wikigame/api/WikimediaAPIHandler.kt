package de.dhbw.wikigame.api

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.dhbw.wikigame.util.DateUtil
import okhttp3.*
import java.io.IOException

class WikimediaAPIHandler {

    private val httpClient = OkHttpClient()

    public fun getMostViewedArticles() {

        val getRequestDatePattern = DateUtil().getRequestFormattedDatePattern()
        val wikimediaBaseURL =
            "https://wikimedia.org/api/rest_v1/metrics/pageviews/top/de.wikipedia/all-access/"
        val requestURL = wikimediaBaseURL + getRequestDatePattern

        val articleRequest = Request.Builder()
            .url(requestURL)
            .header("Accept", "application/json").build()

        println("requestURL: $requestURL")

        httpClient.newCall(articleRequest).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")


                    val moshiInstance: Moshi =
                        Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
                    val parameterizedType = Types.newParameterizedType(
                        List::class.java,
                        WikipediaArticleStatsEntry::class.java
                    )

                    val jsonAdapter: JsonAdapter<List<WikipediaArticleStatsEntry>> =
                        moshiInstance.adapter(parameterizedType)

                    val articleList: List<WikipediaArticleStatsEntry> =
                        jsonAdapter.fromJson(response.body!!.string())!!

                    for (article in articleList) {
                        println(article.articleName)
                    }

                }
            }

        })


    }

}