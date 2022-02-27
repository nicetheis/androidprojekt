package de.dhbw.wikigame

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.dhbw.wikigame.api.wikimedia.interfaces.WikimediaStatsInterface
import android.os.SharedMemory
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import de.dhbw.wikigame.databinding.ActivityInitBinding
import android.net.ConnectivityManager




private lateinit var binding: ActivityInitBinding

class InitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("playerSettings", MODE_PRIVATE)
        val editor = sharedPref.edit()

        val letName = sharedPref.getString("name", null)
        val lswDif = sharedPref.getBoolean("difficulty", false)
        val lswTime = sharedPref.getBoolean("time", false)

        binding.editTextPlayerName.setText(letName)
        binding.switchDifficulty.isChecked = lswDif
        binding.switchTime.isChecked = lswTime

        binding.buttonStart.setOnClickListener {
            val etName = binding.editTextPlayerName.text.toString()
            val swDif = binding.switchDifficulty.isChecked
            val swTime = binding.switchTime.isChecked

            editor.apply {
                putString("name", etName)
                putBoolean("difficulty", swDif)
                putBoolean("time", swTime)
                apply()
            }
            gameStart()
        }
    }

    private fun gameStart() {
        val intent = Intent(this, HigherLowerActivity::class.java)
        startActivity(intent)
    }
}