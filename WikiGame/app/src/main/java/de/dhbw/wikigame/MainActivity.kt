package de.dhbw.wikigame

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuInflater
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import de.dhbw.wikigame.api.wikimedia.handlers.mostviewed.MostViewedArticlesAPIHandler
import de.dhbw.wikigame.util.InternetUtil

class MainActivity : AppCompatActivity() {

    override fun onBackPressed() {
        // prevent back button pressing
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        

        val mostViewedArticlesAPIHandler: MostViewedArticlesAPIHandler =
            MostViewedArticlesAPIHandler("de")


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

        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object: Runnable {
            override fun run() {
                if(!InternetUtil.isInternetAvailable(applicationContext)) {
                    val intent = Intent(applicationContext, InternetWarningActivity::class.java)
                    if(!InternetWarningActivity.isActive) {
                        startActivity(intent)
                    }
                }
                mainHandler.postDelayed(this, 1000)
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }
}