package com.example.learnmorse

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class TrainInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train_info)

        val imageView: ImageView = findViewById(R.id.imageView)
        imageView.setImageResource(R.drawable.ic_goal)

        val closeButton: ImageView = findViewById(R.id.close)
        closeButton.setOnClickListener {
            val intent = Intent(this, TrainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }
    }
}