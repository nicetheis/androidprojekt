package de.dhbw.wikigame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.dhbw.wikigame.api.WikimediaAPIHandler
import de.dhbw.wikigame.api.WikipediaArticleStatsEntry
import de.dhbw.wikigame.util.DateUtil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        WikimediaAPIHandler().getMostViewedArticles()
    }
}