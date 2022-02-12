package de.dhbw.wikigame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.dhbw.wikigame.api.wikimediahandlers.mostviewed.MostViewedArticlesAPIHandler

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MostViewedArticlesAPIHandler().getMostViewedArticles()
    }
}