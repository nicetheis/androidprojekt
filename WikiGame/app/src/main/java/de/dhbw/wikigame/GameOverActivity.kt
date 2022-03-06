package de.dhbw.wikigame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import de.dhbw.wikigame.api.wikimedia.handlers.mostviewed.MostViewedArticlesAPIHandler
import de.dhbw.wikigame.database.Database
import de.dhbw.wikigame.databinding.ActivityGameOverBinding
import de.dhbw.wikigame.highscore.Highscore
import de.dhbw.wikigame.highscore.HighscoreAdapter
import de.dhbw.wikigame.highscore.HighscoreDao
import de.dhbw.wikigame.util.WikipediaLanguage
import okhttp3.internal.notify
import java.lang.Exception
import java.net.InetAddress

private lateinit var binding: ActivityGameOverBinding
private val scoreList: MutableList<Highscore> = mutableListOf()
private lateinit var scoreAdapter: HighscoreAdapter
private lateinit var db: Database
private lateinit var scoreDao: HighscoreDao


class GameOverActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameOverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isInternetAvailable()

        val currentWikiLanguage = WikipediaLanguage.DE
        val mostViewedArticlesAPIHandler: MostViewedArticlesAPIHandler =
        MostViewedArticlesAPIHandler(currentWikiLanguage)

        binding.tvDBEmpty.isVisible = false
        binding.tvDelete.isVisible = false
        binding.tvNotPlayed.isVisible = false

        //Score aus Intent
        val score = intent.getIntExtra("score", 0)
        binding.tvScore.setText(score.toString())

        val sharedPref = getSharedPreferences("playerSettings", MODE_PRIVATE)
        val name = sharedPref.getString("name", "player")
        val time = sharedPref.getBoolean("time", false)
        val difficulty = sharedPref.getBoolean("difficulty", false)
        val country = "de"
        val scoreToInsert = Highscore(name!!, score, time, difficulty, country)

        //Datenbank stuff
        db = Room.databaseBuilder(this, Database::class.java, "highscores")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

        scoreDao = db.getHighscoreDao()

        if(scoreDao.getAll().isEmpty()){
            scoreDao.insertOne(scoreToInsert)
            scoreList.add(scoreToInsert)
            Toast.makeText(this, R.string.score_gespeichert, Toast.LENGTH_SHORT).show()
        } else {
            scoreDao.insertOne(scoreToInsert)
            scoreList.removeAll(scoreList)
            scoreList.addAll(scoreDao.getAllSortedDESC())
            Toast.makeText(this, R.string.score_gespeichert, Toast.LENGTH_SHORT).show()
        }

        //Recyclerview stuff
        binding.rvScores.setLayoutManager(LinearLayoutManager(this, RecyclerView.VERTICAL, false))
        scoreAdapter = HighscoreAdapter(scoreList)
        binding.rvScores.setAdapter(scoreAdapter)

        //RestartButton
        binding.btnRestart.setOnClickListener {
            val intent = Intent(this, HigherLowerActivity::class.java)
            intent.putExtra(
                "mostViewedArticlesJSONString",
                mostViewedArticlesAPIHandler.getMostViewedArticlesJSONString()
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        binding.btnDelete.setOnClickListener {
            if(!scoreList.isEmpty()){
                scoreDao.deleteAll()
                scoreList.removeAll(scoreList)
                scoreAdapter.notifyDataSetChanged()
                Toast.makeText(this, R.string.delete_info, Toast.LENGTH_LONG).show()
                binding.btnDelete.isVisible = false
                binding.radioGroup.isVisible = false
                binding.tvDBEmpty.isVisible = true
                binding.tvRanking.isVisible = false
                binding.tvDelete.isVisible = true
            }
        }
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
                //Flag: Geht zur MainActivity, löscht andere Stack seiten
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked
            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radioAll ->
                    if (checked) {
                        getData(scoreDao.getAllSortedDESC())
                    }
                R.id.radioTime ->
                    if (checked) {
                        getData(scoreDao.getAllForTimeSortedDESC())
                    }
                R.id.radioEasy ->
                    if (checked) {
                        getData(scoreDao.getAllForEasySortedDESC())
                    }
                R.id.radioHeavy ->
                    if (checked) {
                        getData(scoreDao.getAllForHeavySortedDESC())
                    }
            }
        }
    }

    fun getData(scoreListElements: List<Highscore>){
        scoreList.removeAll(scoreList)
        scoreList.addAll(scoreListElements)
        if(scoreList.isEmpty()){
            binding.rvScores.isVisible = false
            binding.tvNotPlayed.isVisible = true
        } else {
            binding.tvNotPlayed.isVisible = false
            binding.rvScores.isVisible = true
        }
        scoreAdapter.notifyDataSetChanged()
    }

    fun isInternetAvailable(): Boolean {
        return try {
            val ipAddr: InetAddress = InetAddress.getByName("google.com")
            !ipAddr.equals("")
        } catch (e: Exception) {
            false
        }
    }
}
