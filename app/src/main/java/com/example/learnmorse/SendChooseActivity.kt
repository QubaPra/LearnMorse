package com.example.learnmorse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class SendChooseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_choose)

        val home = findViewById<ImageView>(R.id.home)

        home.setOnClickListener {
            finish()
        }
    }
}