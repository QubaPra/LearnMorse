package com.example.learnmorse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val testButton: Button = findViewById(R.id.test)
        testButton.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
        }

        val naukaButton: Button = findViewById(R.id.nauka)
        naukaButton.setOnClickListener {
            val intent = Intent(this, LearnActivity::class.java)
            startActivity(intent)
        }

        val nadajButton: Button = findViewById(R.id.nadaj)
        nadajButton.setOnClickListener {
            val intent = Intent(this, SendChooseActivity::class.java)
            startActivity(intent)
        }

        val treningButton: Button = findViewById(R.id.trening)
        treningButton.setOnClickListener {
            val intent = Intent(this, TrainActivity::class.java)
            startActivity(intent)
        }
    }
}