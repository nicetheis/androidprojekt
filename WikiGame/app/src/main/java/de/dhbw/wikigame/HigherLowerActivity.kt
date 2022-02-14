package de.dhbw.wikigame

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.concurrent.schedule

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
        val checkmark = findViewById<ImageView>(R.id.checkmark)


        higherBtn.setOnClickListener {

            Picasso.get()
                .load("https://upload.wikimedia.org/wikipedia/commons/thumb/5/55/Al-Farabi.jpg/80px-Al-Farabi.jpg")
                .into(thumb1)

            if (isHigher()) {
                increaseScore(scoreView, checkmark)
            } else {
                gameOver(score)
            }
        }

        lowerBtn.setOnClickListener {
            if (!isHigher()) {
                increaseScore(scoreView, checkmark)
            } else {
                gameOver(score)
            }
        }
    }

    fun isHigher(): Boolean {
        //hier die Aufrufzahlen beider Werte vergleichen
        return true
    }

    fun increaseScore(scoreView: TextView, checkmark: ImageView){
        score++
        scoreView.text = score.toString()
        checkmark.visibility = View.VISIBLE

        Timer("CheckMarkTimer", false).schedule(500){
            checkmark.visibility = View.INVISIBLE
        }
    }

    fun gameOver(score: Int){
        // navigate to GameOverActivity and send the score
        val intent = Intent(this, GameOverActivity::class.java)
        intent.putExtra("score", score)
        startActivity(intent)
    }
}