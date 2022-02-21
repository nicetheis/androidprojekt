package de.dhbw.wikigame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button1 = findViewById<Button>(R.id.btnTestHigherLower)
        val button2 = findViewById<Button>(R.id.btnTestGameOverScreen)
        val button3 = findViewById<Button>(R.id.btnTestInit)

        button1.setOnClickListener {
            val intent = Intent(this, HigherLowerActivity::class.java)
            startActivity(intent)
        }

        button2.setOnClickListener{
            val intent = Intent(this, GameOverActivity::class.java)
            startActivity(intent)
        }

        button3.setOnClickListener {
            val intent = Intent(this, InitActivity::class.java)
            startActivity(intent)
        }
    }
}