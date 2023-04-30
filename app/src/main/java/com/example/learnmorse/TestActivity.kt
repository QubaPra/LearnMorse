package com.example.learnmorse

import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity



class TestActivity : AppCompatActivity() {



    private lateinit var buttonDot: Button
    private lateinit var buttonMinus: Button
    private lateinit var textViewResult: TextView
    private lateinit var textViewRandom: TextView
    private lateinit var layout: RelativeLayout
    private lateinit var home: ImageView

    private var mediaPlayer: MediaPlayer? = null
    private val mediaQueue: MutableList<MediaPlayer> = mutableListOf()

    private var randomChar = ('A'..'Z').random()
    private var morseCode = getMorseCode(randomChar)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_activity)

        buttonDot = findViewById(R.id.button_dot)
        buttonMinus = findViewById(R.id.button_minus)
        textViewResult = findViewById(R.id.textview_result)
        textViewRandom = findViewById(R.id.textview_random)
        layout = findViewById(R.id.content)
        home = findViewById(R.id.home)
        val sound = findViewById<ImageView>(R.id.sound)
        if (!isMuted) {
            sound.setImageResource(R.drawable.ic_unmute)
        } else {
            sound.setImageResource(R.drawable.ic_mute)
        }

        home.setOnClickListener {
            finish()
        }

        sound.setOnClickListener {
            if (isMuted) {
                sound.setImageResource(R.drawable.ic_unmute)
            } else {
                sound.setImageResource(R.drawable.ic_mute)
            }
            isMuted = !isMuted
        }

        textViewRandom.text = randomChar.toString()

        buttonDot.setOnClickListener {
            if (textViewResult.text.length < 4) {
                textViewResult.append("•")
                if (!isMuted) {
                    val media = MediaPlayer.create(this, R.raw.dot)
                    mediaQueue.add(media)
                    playMediaQueue()
                }
            }
            else {
                checkResult()
            }
        }
        buttonMinus.setOnClickListener {
            if (textViewResult.text.length < 4){
                textViewResult.append("—")
                if (!isMuted) {
                    val media = MediaPlayer.create(this, R.raw.minus)
                    mediaQueue.add(media)
                    playMediaQueue()
                }
            }
            else {
                checkResult()
            }

        }

        layout.setOnClickListener {
            checkResult()
        }
    }
    private fun checkResult() {
        val input = textViewResult.text.toString()
        if (input == morseCode) {
            textViewRandom.setTextColor(getColor(R.color.accent1))
        } else {
            textViewRandom.setTextColor(getColor(R.color.accent2))
        }
        buttonMinus.isClickable = false
        buttonDot.isClickable = false
        layout.isClickable = false
        Handler(Looper.getMainLooper()).postDelayed({
            textViewResult.text = ""
            randomChar = ('A'..'Z').random()
            textViewRandom.text = randomChar.toString()
            val configuration = resources.configuration
            val isDarkMode = configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
            if (isDarkMode) {
                textViewRandom.setTextColor(getColor(R.color.white))
            } else {
                textViewRandom.setTextColor(getColor(R.color.white))
            }
            morseCode = getMorseCode(randomChar)
            buttonMinus.isClickable = true
            buttonDot.isClickable = true
            layout.isClickable = true
        }, 1000)
    }

    override fun onStop() {
        super.onStop()
        for (media in mediaQueue) {
            media.release()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        lastSignal = 0
    }
    private fun playMediaQueue() {
        if (mediaQueue.isNotEmpty() && mediaPlayer == null) {
            mediaPlayer = mediaQueue.removeAt(0)
            mediaPlayer?.setOnCompletionListener {
                mediaPlayer?.release()
                mediaPlayer = null
                playMediaQueue()
            }
            mediaPlayer?.start()
        }
    }
}
