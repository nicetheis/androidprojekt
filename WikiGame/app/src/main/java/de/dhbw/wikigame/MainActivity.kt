package de.dhbw.wikigame

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import de.dhbw.wikigame.api.wikimedia.handlers.mostviewed.MostViewedArticlesAPIHandler
import de.dhbw.wikigame.api.wikimedia.interfaces.WikimediaStatsInterface
import de.dhbw.wikigame.api.wikipedia.handlers.images.ArticleThumbnailAPIHandler
import android.net.ConnectivityManager
import java.lang.Exception
import java.net.InetAddress
import java.net.UnknownHostException


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        isInternetAvailable()

        val mostViewedArticlesAPIHandler: MostViewedArticlesAPIHandler =
            MostViewedArticlesAPIHandler()


        val button1 = findViewById<Button>(R.id.btnTestHigherLower)
        val button2 = findViewById<Button>(R.id.btnTestGameOverScreen)
        val button3 = findViewById<Button>(R.id.btnTestInit)

        button1.setOnClickListener {
            val intent = Intent(this, HigherLowerActivity::class.java)
            intent.putExtra(
                "mostViewedArticlesJSONString",
                mostViewedArticlesAPIHandler.getMostViewedArticlesJSONString()
            )
            startActivity(intent)
        }

        button2.setOnClickListener {
            val intent = Intent(this, GameOverActivity::class.java)
            startActivity(intent)
        }

        button3.setOnClickListener {
            val intent = Intent(this, InitActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
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