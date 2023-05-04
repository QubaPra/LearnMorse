package com.example.learnmorse

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class RulesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rules)

        val home = findViewById<ImageView>(R.id.home)
        home.setOnClickListener {
            finish()
        }
    }
}