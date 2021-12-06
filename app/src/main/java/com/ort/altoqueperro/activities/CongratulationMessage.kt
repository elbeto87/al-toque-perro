package com.ort.altoqueperro.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import com.ort.altoqueperro.R

class CongratulationMessage : AppCompatActivity() {

    private val SPLASH_TIME_OUT:Long = 2500
    private lateinit var txtCongrat : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_congratulation_message)
        txtCongrat = findViewById(R.id.txtCongratulation)

        txtCongrat.text = "Nos alegra que hayas encontrado a {nombre mascota} "

        Handler().postDelayed(
            {
                startActivity(Intent(this,HomeNavigationActivity::class.java))
                finish()
            }
            , SPLASH_TIME_OUT)
    }
}