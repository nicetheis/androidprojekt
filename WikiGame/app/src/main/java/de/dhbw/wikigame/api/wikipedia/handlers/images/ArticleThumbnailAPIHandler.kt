package de.dhbw.wikigame.api.wikipedia.handlers.images

import androidx.lifecycle.MutableLiveData
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.dhbw.wikigame.HigherLowerActivityViewModel
import de.dhbw.wikigame.api.wikipedia.datatypes.WikipediaQueryResult
import de.dhbw.wikigame.api.wikipedia.datatypes.WikipediaThumbnail
import de.dhbw.wikigame.util.WikipediaLanguage
import okhttp3.*
import java.io.IOException
import java.net.URLEncoder

class ArticleThumbnailAPIHandler(val currentWikiLanguage: WikipediaLanguage) {

    private val httpClient = OkHttpClient()

    private fun getImageURLByArticleName(articleName: String): String {

        val whiteSpaceEncodedURLSearchString = URLEncoder.encode(articleName, "UTF-8").replace("+", "%20")

        return "https://${(currentWikiLanguage.toString().lowercase())}.wikipedia.org/w/api.php?action=query&formatversion=2&prop=pageimages%7Cpageterms&titles=${whiteSpaceEncodedURLSearchString}&format=json&pithumbsize=500"
    }

    fun getWikipediaArticleThumbnailURL(
        articleName: String,
        mutableData: MutableLiveData<String>
    ) {

        val requestURL = getImageURLByArticleName(articleName)
        val thumbnailRequest = Request.Builder()
            .url(requestURL)
            .header("Accept", "application/json").build()

        println("searching thumbnail for article: $articleName")
        println("using requestURL: $requestURL")

        httpClient.newCall(thumbnailRequest).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {

                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val moshiInstance: Moshi =
                        Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
                    val jsonAdapter: JsonAdapter<WikipediaQueryResult> =
                        moshiInstance.adapter(WikipediaQueryResult::class.java)
                    val responseBody: ResponseBody = response.body!!
                    val responseBodyContent: String = responseBody.string()

                    var thumbURL = ""

                    thumbURL = try {
                        var wikipediaQueryResult: WikipediaQueryResult =
                            jsonAdapter.fromJson(responseBodyContent)!!
                        wikipediaQueryResult.query.pages[0].thumbnail.source
                    } catch (jsonDataException: JsonDataException) {
                        "https://upload.wikimedia.org/wikipedia/commons/6/61/Wikipedia-logo-transparent.png"
                    }

                    mutableData.postValue(thumbURL)

                }
            }

        })

    }

}