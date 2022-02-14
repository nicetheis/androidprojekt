package de.dhbw.wikigame.api.wikimediahandlers.mostviewed

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.dhbw.wikigame.api.wikimediadatatypes.WikimediaData
import de.dhbw.wikigame.util.WikimediaDateUtil
import okhttp3.*
import java.io.IOException

class MostViewedArticlesAPIHandler {

    private val httpClient = OkHttpClient()

    private fun getMostViewedArticlesRequestURL(): String {

        val getRequestDatePattern = WikimediaDateUtil().getGETRequestFormattedDatePattern()
        val wikimediaBaseURL =
            "https://wikimedia.org/api/rest_v1/metrics/pageviews/top/de.wikipedia/all-access/"
        return wikimediaBaseURL + getRequestDatePattern

    }

    fun getMostViewedArticles() {

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

                    val moshiInstance: Moshi =
                        Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
                    val jsonAdapter: JsonAdapter<WikimediaData> =
                        moshiInstance.adapter(WikimediaData::class.java)
                    val responseBody: ResponseBody = response.body!!
                    val responseBodyContent: String = responseBody.string()
                    var wikimediaData: WikimediaData = jsonAdapter.fromJson(responseBodyContent)!!

                    // TODO: implement function return value -> some more work needed because get-request runs async, simple function return value therefore not possible.

                }
            }

        })

    }

}