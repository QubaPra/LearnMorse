package com.example.learnmorse

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SpeedStartActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speed_start)

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)


        val best_score = findViewById<TextView>(R.id.best_score)
        best_score.text = bestScore

        val home = findViewById<ImageView>(R.id.home)
        home.setOnClickListener {
            finish()
        }
        val start = findViewById<Button>(R.id.button)
        start.setOnClickListener {
            val intent = Intent(this, SpeedActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }
        val info = findViewById<ImageView>(R.id.info)
        info.setOnClickListener {
            isSpeedTutorial = true
            val intent = Intent(this, SpeedActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }
    }
    override fun onPause() {
        super.onPause()

        // Save values for lightSpeedSetting, learn_alphabet and train_alphabet to SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putBoolean("isSpeedTutorial", isSpeedTutorial)
        editor.apply()
    }
}