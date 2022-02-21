package de.dhbw.wikigame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import de.dhbw.wikigame.database.Database
import de.dhbw.wikigame.databinding.ActivityGameOverBinding
import de.dhbw.wikigame.highscore.Highscore
import de.dhbw.wikigame.highscore.HighscoreAdapter
import okhttp3.internal.notify

private lateinit var binding: ActivityGameOverBinding
private val scoreList: MutableList<Highscore> = mutableListOf()
private lateinit var scoreAdapter: HighscoreAdapter


class GameOverActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameOverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Score aus Intent
        val score = intent.getIntExtra("score", 0)
        binding.tvScore.setText(score.toString())

        val sharedPref = getSharedPreferences("playerSettings", MODE_PRIVATE)
        val name = sharedPref.getString("name", "player")
        val scoreToInsert = Highscore(name!!, score)

        //Datenbank stuff
        val db = Room.databaseBuilder(this, Database::class.java, "highscores")
            .allowMainThreadQueries()
            .build()

        val scoreDao = db.getHighscoreDao()

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
            finish()
        }

        binding.btnDelete.setOnClickListener {
            if(!scoreList.isEmpty()){
                scoreDao.deleteAll()
                scoreList.removeAll(scoreList)
                scoreAdapter.notifyDataSetChanged()
                Toast.makeText(this, R.string.delete_info, Toast.LENGTH_LONG).show()
                binding.btnDelete.isVisible = false
                binding.tvRanking.setText(R.string.delete_info)
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
}
