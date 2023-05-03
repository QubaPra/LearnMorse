package com.example.learnmorse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class SpeedStartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speed_start)

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
            val intent = Intent(this, SpeedInfoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }

    }
}