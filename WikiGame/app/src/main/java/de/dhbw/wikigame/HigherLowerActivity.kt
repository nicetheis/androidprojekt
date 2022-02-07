package de.dhbw.wikigame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class HigherLowerActivity : AppCompatActivity() {
    var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_higher_lower)

        val scoreView = findViewById<TextView>(R.id.scoreValue)
        val higherBtn = findViewById<Button>(R.id.higherButton)
        val lowerBtn = findViewById<Button>(R.id.lowerButton)
        val thumb1 = findViewById<ImageView>(R.id.thumbnail1)
        val thumb2 = findViewById<ImageView>(R.id.thumbnail2)


        higherBtn.setOnClickListener {

            Picasso.get()
                .load("https://upload.wikimedia.org/wikipedia/commons/thumb/5/55/Al-Farabi.jpg/80px-Al-Farabi.jpg")
                .into(thumb1)

            if (isHigher()) {
                increaseScore(scoreView)
            } else {
                gameOver()
            }
        }

        lowerBtn.setOnClickListener {
            if (!isHigher()) {
                increaseScore(scoreView)
            } else {
                gameOver()
            }
        }
    }

    fun isHigher(): Boolean {
        //hier die Aufrufzahlen beider Werte vergleichen
        return true
    }

    fun increaseScore(scoreView: TextView){
        score++
        scoreView.text = score.toString();
    }

    fun gameOver(){
        val intent = Intent(this, GameOverActivity::class.java)
        startActivity(intent)
    }
}