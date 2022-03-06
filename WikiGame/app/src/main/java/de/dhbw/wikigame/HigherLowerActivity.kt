package de.dhbw.wikigame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import de.dhbw.wikigame.api.wikimedia.datatypes.WikimediaArticleStatistics
import de.dhbw.wikigame.api.wikimedia.interfaces.WikimediaStatsInterface
import de.dhbw.wikigame.api.wikipedia.handlers.images.ArticleThumbnailAPIHandler
import de.dhbw.wikigame.databinding.ActivityHigherLowerBinding
import java.lang.Exception
import java.net.InetAddress
import java.util.*
import kotlin.concurrent.schedule

private lateinit var binding: ActivityHigherLowerBinding
private lateinit var viewModel: HigherLowerActivityViewModel
private lateinit var article1: WikimediaArticleStatistics
private lateinit var article2: WikimediaArticleStatistics
private lateinit var wikimediaStatsInterface: WikimediaStatsInterface
private var isTimeMode = false
private var isHighDifficulty = false
private var isUpperBound = true
private var score: Int = 0
private var timerLiveData: MutableLiveData<Int> = MutableLiveData(6)
private var isGameOver = false
private var currentWikiLanguage = ""

class HigherLowerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHigherLowerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(HigherLowerActivityViewModel::class.java)

        //set initial values
        score = 0
        timerLiveData = MutableLiveData(6)
        isGameOver = false

        //handle gamemodes
        val sharedPref = getSharedPreferences("playerSettings", MODE_PRIVATE)
        //timed mode
        isTimeMode = sharedPref.getBoolean("time", false)
        isHighDifficulty = sharedPref.getBoolean("difficulty", false)
        currentWikiLanguage = sharedPref.getString("country", "de")!!

        //get data from wikipedia api
        val mostViewedArticlesJSONString: String =
            intent.getStringExtra("mostViewedArticlesJSONString")!!
        wikimediaStatsInterface = WikimediaStatsInterface(mostViewedArticlesJSONString, "{}")

        //initialize game with first two articles
        if (isHighDifficulty) {
            article1 = (wikimediaStatsInterface.getRandomWikiArticle())!!
            article2 = (wikimediaStatsInterface.getRandomWikiArticle())!!
        } else {
            article1 = (wikimediaStatsInterface.getRandomWikiArticleUpperBound())!!
            article2 = (wikimediaStatsInterface.getRandomWikiArticleLowerBound())!!
        }

        loadArticlesToView(article1, article2)

        //buttons
        binding.higherButton.setOnClickListener {
            if (article1!!.views < article2!!.views) {
                increaseScore()
                displayCheckmark()
                showNewArticles()
                if (isTimeMode) timerLiveData.postValue(5)
            } else gameOver()
        }
        binding.lowerButton.setOnClickListener {
            if (article1!!.views > article2!!.views) {
                increaseScore()
                displayCheckmark()
                showNewArticles()
                if (isTimeMode) timerLiveData.postValue(5)
            } else gameOver()
        }


        if (isTimeMode) {
            timerLiveData.value = 6
            binding.timerText.visibility = View.VISIBLE
            binding.timerValue.visibility = View.VISIBLE
            binding.timerValue.text = timerLiveData.value.toString()
            timerLiveData.observe(this, Observer {
                binding.timerValue.text = timerLiveData.value.toString()
                if (!isGameOver && timerLiveData.value!! == 0) {
                    binding.timerValue.visibility = View.INVISIBLE
                    binding.timeIsUp.visibility = View.VISIBLE
                    gameOver()
                }
            })
            decrementTimer()
        }
    }

    //game logic
    fun increaseScore() {
        score++
        binding.scoreValue.text = score.toString()
    }

    fun displayCheckmark() {
        //display checkmark for 0.5s
        binding.checkmark.visibility = View.VISIBLE
        Timer("CheckMarkTimer", false).schedule(500) {
            binding.checkmark.visibility = View.INVISIBLE
        }
    }

    fun showNewArticles() {
        article1 = article2
        if (isHighDifficulty) {
            article2 = (wikimediaStatsInterface.getRandomWikiArticle())!!
        } else {
            if (isUpperBound) {
                article2 = (wikimediaStatsInterface.getRandomWikiArticleUpperBound())!!
            } else {
                article2 = (wikimediaStatsInterface.getRandomWikiArticleLowerBound())!!
            }
        }
        loadArticlesToView(article1, article2)
    }

    fun loadArticlesToView(
        article1: WikimediaArticleStatistics?,
        article2: WikimediaArticleStatistics?
    ) {
        viewModel.currentFirstArticleThumbnailURL.observe(this, Observer {
            println("observe thumb1: ${viewModel.currentFirstArticleThumbnailURL.value}")
            Picasso.get()
                .load(viewModel.currentFirstArticleThumbnailURL.value)
                .into(binding.thumbnail1)
            binding.lable1.text = article1!!.article.replace("_", " ")
            binding.viewCount1.text = article1!!.views.toString()
        })
        viewModel.currentSecondArticleThumbnailURL.observe(this, Observer {
            println("observe thumb2: ${viewModel.currentSecondArticleThumbnailURL.value}")
            Picasso.get()
                .load(viewModel.currentSecondArticleThumbnailURL.value)
                .into(binding.thumbnail2)
            binding.lable2.text = article2!!.article.replace("_", " ")
        })
        //handle loading thumbnails
        val articleThumbnailAPIHandler = ArticleThumbnailAPIHandler(currentWikiLanguage)
        articleThumbnailAPIHandler.getWikipediaArticleThumbnailURL(
            article1!!.article,
            viewModel.currentFirstArticleThumbnailURL
        )
        articleThumbnailAPIHandler.getWikipediaArticleThumbnailURL(
            article2!!.article,
            viewModel.currentSecondArticleThumbnailURL
        )
    }

    fun gameOver() {
        isGameOver = true

        //display red x and views of article2
        binding.redx.visibility = View.VISIBLE
        binding.viewCount2.text = article2!!.views.toString()

        val intent = Intent(this, GameOverActivity::class.java)
        //wait 2 seconds
        Timer("WaitGameOverTimer", false).schedule(2000) {
            //navigate to GameOverActivity and send the score
            intent.putExtra("score", score)
            startActivity(intent)
            finish()
        }
    }

    //timed mode
    fun decrementTimer() {
        if (timerLiveData.value!! > 0) {
            timerLiveData.value?.let { value -> timerLiveData.postValue(value - 1) }
            Timer("WaitTimer", false).schedule(1000) {
                decrementTimer()
            }
        }
    }

    //Menu bar
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