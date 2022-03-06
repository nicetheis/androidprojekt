package de.dhbw.wikigame

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import de.dhbw.wikigame.api.wikimedia.handlers.mostviewed.MostViewedArticlesAPIHandler
import de.dhbw.wikigame.databinding.ActivityInitBinding

private lateinit var binding: ActivityInitBinding

class InitActivity : AppCompatActivity() {

    override fun onBackPressed() {
        // prevent back button pressing
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("playerSettings", MODE_PRIVATE)
        val editor = sharedPref.edit()
        
        val letName = sharedPref.getString("name", null)
        val lswDif = sharedPref.getBoolean("difficulty", false)
        val lswTime = sharedPref.getBoolean("time", false)
        val lrbCountry: String = sharedPref.getString("country", "de")!!

        if (lrbCountry == "fr") {
            binding.radioGroupCountry.check(R.id.radioButtonCountryFrance)
        } else if (lrbCountry == "en") {
            binding.radioGroupCountry.check(R.id.radioButtonCountryUK)
        } else {
            binding.radioGroupCountry.check(R.id.radioButtonCountryGermany)
        }

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

            if (etName.trim() == ("")) {
                Toast.makeText(applicationContext, "Bitte gib deinen Namen ein", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, HigherLowerActivity::class.java)
                startActivity(intent)
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

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.radioButtonCountryGermany ->
                    if (checked) {
                        val sharedPref = getSharedPreferences("playerSettings", MODE_PRIVATE)
                        val editor = sharedPref.edit()
                        editor.apply {
                            putString("country", "de")
                            apply()
                        }
                    }
                R.id.radioButtonCountryFrance ->
                    if (checked) {
                        if (checked) {
                            val sharedPref = getSharedPreferences("playerSettings", MODE_PRIVATE)
                            val editor = sharedPref.edit()
                            editor.apply {
                                putString("country", "fr")
                                apply()
                            }
                        }
                    }
                R.id.radioButtonCountryUK ->
                    if (checked) {
                        if (checked) {
                            val sharedPref = getSharedPreferences("playerSettings", MODE_PRIVATE)
                            val editor = sharedPref.edit()
                            editor.apply {
                                putString("country", "en")
                                apply()
                            }
                        }
                    }
            }
        }
    }
}