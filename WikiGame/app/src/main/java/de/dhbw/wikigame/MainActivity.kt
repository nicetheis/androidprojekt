package de.dhbw.wikigame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button1 = findViewById<Button>(R.id.btnTestHigherLower)
        val button2 = findViewById<Button>(R.id.btnTestGameOverScreen)

        button1.setOnClickListener {
            val intent = Intent(this, HigherLowerActivity::class.java)
            startActivity(intent)
        }

        button2.setOnClickListener{
            val intent = Intent(this, GameOverActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.icHome -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}