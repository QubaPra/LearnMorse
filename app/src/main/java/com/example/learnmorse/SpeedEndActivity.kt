package com.example.learnmorse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class SpeedEndActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speed_end)

        if (bestScore.filter { it.isDigit() }.toInt() < score.filter { it.isDigit() }.toInt()) {
            bestScore = score
        }

        val last_score = findViewById<TextView>(R.id.last_score)
        val best_score = findViewById<TextView>(R.id.best_score)

        last_score.text = score
        best_score.text = bestScore

        val home = findViewById<ImageView>(R.id.home)
        home.setOnClickListener {
            finish()
        }

        val info = findViewById<ImageView>(R.id.info)
        info.setOnClickListener {
            val intent = Intent(this, SpeedInfoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }

        val restart = findViewById<Button>(R.id.button)
        restart.setOnClickListener {
            val intent = Intent(this, SpeedActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }
    }
}