package de.dhbw.wikigame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import de.dhbw.wikigame.api.wikimedia.interfaces.WikimediaStatsInterface
import de.dhbw.wikigame.api.wikipedia.handlers.images.ArticleThumbnailAPIHandler
import de.dhbw.wikigame.databinding.ActivityHigherLowerBinding
import java.util.*
import kotlin.concurrent.schedule

private lateinit var binding: ActivityHigherLowerBinding

class HigherLowerActivity : AppCompatActivity() {
    var score = 0

    lateinit var viewModel: HigherLowerActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHigherLowerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mostViewedArticlesJSONString: String = intent.getStringExtra("mostViewedArticlesJSONString")!!

        val wikimediaStatsInterface: WikimediaStatsInterface = WikimediaStatsInterface(mostViewedArticlesJSONString, "{}")

        var article1 = wikimediaStatsInterface.getRandomWikiArticle()
        var article2 = wikimediaStatsInterface.getRandomWikiArticle()

        viewModel = ViewModelProvider(this).get(HigherLowerActivityViewModel::class.java)

        viewModel.currentFirstArticleThumbnailURL.observe(this, Observer{

            println("observe thumb1: ${viewModel.currentFirstArticleThumbnailURL.value}")
            Picasso.get()
                .load(viewModel.currentFirstArticleThumbnailURL.value)
                .into(binding.thumbnail1)
            binding.lable1.text = article1!!.article
            binding.viewCount1.text = article1!!.views.toString()

        })

        viewModel.currentSecondArticleThumbnailURL.observe(this, Observer{

            println("observe thumb2: ${viewModel.currentSecondArticleThumbnailURL.value}")
            Picasso.get()
                .load(viewModel.currentSecondArticleThumbnailURL.value)
                .into(binding.thumbnail2)
            binding.lable2.text = article2!!.article
        })


        val articleThumbnailAPIHandler: ArticleThumbnailAPIHandler = ArticleThumbnailAPIHandler()
        articleThumbnailAPIHandler.getWikipediaArticleThumbnailURL(article1!!.article, viewModel.currentFirstArticleThumbnailURL)
        articleThumbnailAPIHandler.getWikipediaArticleThumbnailURL(article2!!.article, viewModel.currentSecondArticleThumbnailURL)



        binding.higherButton.setOnClickListener {
            if (article1!!.views < article2!!.views) {
                increaseScore()
            } else {
                gameOver(score)
            }
        }

        binding.lowerButton.setOnClickListener {
            if (article1!!.views > article2!!.views) {
                increaseScore()
            } else {
                gameOver(score)
            }
        }
    }

    fun increaseScore() {
        score++
        binding.scoreValue.text = score.toString()

        //display checkmark for 0.5s
        binding.checkmark.visibility = View.VISIBLE
        Timer("CheckMarkTimer", false).schedule(500) {
            binding.checkmark.visibility = View.INVISIBLE
        }
    }

    fun gameOver(score: Int){
        // navigate to GameOverActivity and send the score
        val intent = Intent(this, GameOverActivity::class.java)
        intent.putExtra("score", score)
        startActivity(intent)
        finish()
    }

    //Menu stuff
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        menu.findItem(R.id.icHome).setVisible(true)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.icHome -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}