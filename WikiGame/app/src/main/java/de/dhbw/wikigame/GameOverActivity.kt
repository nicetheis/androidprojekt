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
import de.dhbw.wikigame.database.Database
import de.dhbw.wikigame.databinding.ActivityGameOverBinding
import de.dhbw.wikigame.highscore.Highscore
import de.dhbw.wikigame.highscore.HighscoreAdapter
import de.dhbw.wikigame.highscore.HighscoreDao
import okhttp3.internal.notify

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

        binding.tvDBEmpty.isVisible = false
        binding.tvDelete.isVisible = false

        //Score aus Intent
        val score = intent.getIntExtra("score", 0)
        binding.tvScore.setText(score.toString())

        val sharedPref = getSharedPreferences("playerSettings", MODE_PRIVATE)
        val name = sharedPref.getString("name", "player")
        val time = sharedPref.getBoolean("time", false)
        val difficulty = sharedPref.getBoolean("difficulty", false)
        val scoreToInsert = Highscore(name!!, score, time, difficulty)

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
                        scoreList.removeAll(scoreList)
                        scoreList.addAll(scoreDao.getAllSortedDESC())
                        scoreAdapter.notifyDataSetChanged()
                    }
                R.id.radioTime ->
                    if (checked) {
                        scoreList.removeAll(scoreList)
                        scoreList.addAll(scoreDao.getAllForTimeSortedDESC())
                        scoreAdapter.notifyDataSetChanged()
                    }
                R.id.radioEasy ->
                    if (checked) {
                        scoreList.removeAll(scoreList)
                        scoreList.addAll(scoreDao.getAllForEasySortedDESC())
                        scoreAdapter.notifyDataSetChanged()
                    }
                R.id.radioHeavy ->
                    if (checked) {
                        scoreList.removeAll(scoreList)
                        scoreList.addAll(scoreDao.getAllForHeavySortedDESC())
                        scoreAdapter.notifyDataSetChanged()
                    }
            }
        }
    }

}
