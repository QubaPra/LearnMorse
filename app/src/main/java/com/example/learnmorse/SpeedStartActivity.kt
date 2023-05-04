package com.example.learnmorse

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SpeedStartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speed_start)

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
            //uzupełnić
        }
    }
}