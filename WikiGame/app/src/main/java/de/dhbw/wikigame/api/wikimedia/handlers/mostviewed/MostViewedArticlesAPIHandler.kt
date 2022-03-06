package de.dhbw.wikigame.api.wikimedia.handlers.mostviewed

import android.util.Log
import de.dhbw.wikigame.api.wikimedia.interfaces.MostViewedArticlesJSONStorage
import de.dhbw.wikigame.util.WikimediaDateUtil
import okhttp3.*
import java.io.IOException

class MostViewedArticlesAPIHandler() {

    private val httpClient = OkHttpClient()

    init {
        fetchMostViewedArticles("de")
        fetchMostViewedArticles("en")
        fetchMostViewedArticles("fr")
    }

    private fun getMostViewedArticlesRequestURL(currentWikiLanguage: String): String {

        val getRequestDatePattern = WikimediaDateUtil().getGETRequestFormattedDatePattern()
        val wikimediaBaseURL =
            "https://wikimedia.org/api/rest_v1/metrics/pageviews/top/${currentWikiLanguage}.wikipedia/all-access/"
        return wikimediaBaseURL + getRequestDatePattern

    }

    private fun fetchMostViewedArticles(languageString: String) {

        val requestURL = getMostViewedArticlesRequestURL(languageString)
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
                    MostViewedArticlesJSONStorage.mostViewedArticlesJSONStrings.put(
                        languageString,
                        responseBodyContent
                    )
                    Log.d("DEBUG", "Successfully put json string array for lanugage $languageString")
                    Log.d("DEBUG", "String for $languageString: ${MostViewedArticlesJSONStorage.mostViewedArticlesJSONStrings[languageString]!!}}")
                }
            }

        })

    }

}