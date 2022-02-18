package de.dhbw.wikigame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import de.dhbw.wikigame.api.wikimedia.interfaces.WikimediaStatsInterface
import de.dhbw.wikigame.api.wikipedia.handlers.images.ArticleThumbnailAPIHandler
import java.util.*
import kotlin.concurrent.schedule

class HigherLowerActivity : AppCompatActivity() {
    var score = 0

    lateinit var viewModel: HigherLowerActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_higher_lower)

        val mostViewedArticlesJSONString: String = intent.getStringExtra("mostViewedArticlesJSONString")!!

        val wikimediaStatsInterface: WikimediaStatsInterface = WikimediaStatsInterface(mostViewedArticlesJSONString, "{}")

        val article1 = wikimediaStatsInterface.getRandomWikiArticle()
        val article2 = wikimediaStatsInterface.getRandomWikiArticle()

        val scoreView = findViewById<TextView>(R.id.scoreValue)
        val higherBtn = findViewById<Button>(R.id.higherButton)
        val lowerBtn = findViewById<Button>(R.id.lowerButton)
        val thumb1 = findViewById<ImageView>(R.id.thumbnail1)
        val thumb2 = findViewById<ImageView>(R.id.thumbnail2)
        val lable1 = findViewById<TextView>(R.id.lable1)
        val lable2 = findViewById<TextView>(R.id.lable2)
        val checkmark = findViewById<ImageView>(R.id.checkmark)
        val viewCount = findViewById<TextView>(R.id.viewCount1)

        viewModel = ViewModelProvider(this).get(HigherLowerActivityViewModel::class.java)

        viewModel.currentFirstArticleThumbnailURL.observe(this, Observer{

            println("observe thumb1: ${viewModel.currentFirstArticleThumbnailURL.value}")
            Picasso.get()
                .load(viewModel.currentFirstArticleThumbnailURL.value)
                .into(thumb1)
            lable1.text = article1!!.article
            viewCount.text = article1!!.views.toString()

        })

        viewModel.currentSecondArticleThumbnailURL.observe(this, Observer{

            println("observe thumb2: ${viewModel.currentSecondArticleThumbnailURL.value}")
            Picasso.get()
                .load(viewModel.currentSecondArticleThumbnailURL.value)
                .into(thumb2)
            lable2.text = article2!!.article
        })


        val articleThumbnailAPIHandler: ArticleThumbnailAPIHandler = ArticleThumbnailAPIHandler()
        articleThumbnailAPIHandler.getWikipediaArticleThumbnailURL(article1!!.article, viewModel.currentFirstArticleThumbnailURL)
        articleThumbnailAPIHandler.getWikipediaArticleThumbnailURL(article2!!.article, viewModel.currentSecondArticleThumbnailURL)

        // TODO: implement try-catch for wikipedia articles WITHOUT existing thumbnail

        higherBtn.setOnClickListener {

            Picasso.get()
                .load("https://upload.wikimedia.org/wikipedia/commons/thumb/5/55/Al-Farabi.jpg/80px-Al-Farabi.jpg")
                .into(thumb1)

            if (isHigher()) {
                increaseScore(scoreView, checkmark)
            } else {
                gameOver()
            }
        }

        lowerBtn.setOnClickListener {
            if (!isHigher()) {
                increaseScore(scoreView, checkmark)
            } else {
                gameOver()
            }
        }
    }


    fun isHigher(): Boolean {
        //hier die Aufrufzahlen beider Werte vergleichen
        return true
    }

    fun increaseScore(scoreView: TextView, checkmark: ImageView) {
        score++
        scoreView.text = score.toString()
        checkmark.visibility = View.VISIBLE

        Timer("CheckMarkTimer", false).schedule(500) {
            checkmark.visibility = View.INVISIBLE
        }
    }

    fun gameOver() {
        val intent = Intent(this, GameOverActivity::class.java)
        startActivity(intent)
    }
}