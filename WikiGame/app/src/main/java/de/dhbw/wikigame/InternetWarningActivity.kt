package de.dhbw.wikigame

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import de.dhbw.wikigame.util.InternetUtil

class InternetWarningActivity : AppCompatActivity() {

    companion object {
        var isActive = false
    }


    override fun onStart() {
        super.onStart()
        isActive = true
    }

    override fun onStop() {
        super.onStop()
        isActive = false
    }

    override fun onBackPressed() {
        // prevent back button pressing
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_internet_warning)

        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object: Runnable {
            override fun run() {
                if(InternetUtil.isInternetAvailable(applicationContext)) {
                    finish()
                }
                mainHandler.postDelayed(this, 1000)
            }
        })


    }

}