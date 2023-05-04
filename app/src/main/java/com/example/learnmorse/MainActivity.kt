package com.example.learnmorse

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

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
            val intent = Intent(this, SendAutoActivity::class.java)
            startActivity(intent)
        }

        val treningButton: Button = findViewById(R.id.trening)
        treningButton.setOnClickListener {
            val intent = Intent(this, TrainActivity::class.java)
            startActivity(intent)
        }

        val speedButton: Button = findViewById(R.id.speed)
        speedButton.setOnClickListener {
            val intent = Intent(this, SpeedStartActivity::class.java)
            startActivity(intent)
        }

        val zasadyButton: Button = findViewById(R.id.zasady)
        zasadyButton.setOnClickListener {
            val intent = Intent(this, RulesActivity::class.java)
            startActivity(intent)
        }

        val settingsButton: ImageView = findViewById(R.id.settings)
        settingsButton.setOnClickListener {
            val intent = Intent(this, MainSettingsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }
    }
}