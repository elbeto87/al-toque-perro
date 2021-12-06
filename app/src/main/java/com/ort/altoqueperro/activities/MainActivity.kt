package com.ort.altoqueperro.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ort.altoqueperro.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        setContentView(R.layout.activity_main)
    }
}