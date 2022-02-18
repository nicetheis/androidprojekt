package de.dhbw.wikigame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.dhbw.wikigame.api.wikimedia.interfaces.WikimediaStatsInterface

class InitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)
    }
}