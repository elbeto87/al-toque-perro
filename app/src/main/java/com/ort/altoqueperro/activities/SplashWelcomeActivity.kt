package com.ort.altoqueperro.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.ort.altoqueperro.R

class SplashWelcomeActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 2500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        setContentView(R.layout.activity_splash_welcome)

        Handler().postDelayed(
            {
                startActivity(Intent(this, HomeNavigationActivity::class.java))
                finish()
            }, SPLASH_TIME_OUT)
    }
}