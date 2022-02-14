package de.dhbw.wikigame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import de.dhbw.wikigame.database.Database
import de.dhbw.wikigame.databinding.ActivityGameOverBinding
import de.dhbw.wikigame.highscore.Highscore
import de.dhbw.wikigame.highscore.HighscoreAdapter

private lateinit var binding: ActivityGameOverBinding
private val scoreList: MutableList<Highscore> = mutableListOf()
private lateinit var scoreAdapter: HighscoreAdapter


class GameOverActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameOverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Datenbank stuff
        val db = Room.databaseBuilder(this, Database::class.java, "highscores")
            .allowMainThreadQueries()
            .build()

        val scoreDao = db.getHighscoreDao()

        if(scoreDao.getAll().isEmpty()){
            fillScores()
            scoreList.forEach { score ->
                scoreDao.insertOne(score)
            }
        } else {
            scoreList.addAll(scoreDao.getAll())
        }

        //Recyclerview stuff
        binding.rvScores.setLayoutManager(LinearLayoutManager(this, RecyclerView.VERTICAL, false))
        scoreAdapter = HighscoreAdapter(scoreList)
        binding.rvScores.setAdapter(scoreAdapter)
    }

    //Menu stuff
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.icHome -> {
                //Nicht mit Intent --> Sonst Seite aufm Stack
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

private fun fillScores() {
    scoreList.addAll(
        listOf(
            Highscore("Theresa", 999),
            Highscore("Theresa2", 872),
            Highscore("Theresa3", 736),
            Highscore("Markus", 0),
            Highscore("Florian", 0),
            Highscore("Max", -999)
        )
    )
}
