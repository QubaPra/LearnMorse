package com.example.learnmorse

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

var lastLetter = 0
var lastSignal = 0
var isMuted = false

class LearnActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private val mediaQueue: MutableList<MediaPlayer> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)

        val green = ContextCompat.getColor(this, R.color.accent1)
        val red = ContextCompat.getColor(this, R.color.accent2)
        val white = ContextCompat.getColor(this, R.color.white)
        val textViewRandom = findViewById<TextView>(R.id.textview_random)
        val textViewResult = findViewById<TextView>(R.id.textview_result)
        val buttonDot = findViewById<Button>(R.id.button_dot)
        val buttonMinus = findViewById<Button>(R.id.button_minus)
        val home = findViewById<ImageView>(R.id.home)
        val sound = findViewById<ImageView>(R.id.sound)
        if (!isMuted) {
            sound.setImageResource(R.drawable.ic_unmute)
        } else {
            sound.setImageResource(R.drawable.ic_mute)
        }

        // Change text of textViewRandom and textViewResult to 'A' in Morse code
        textViewRandom.text = alphabet[lastLetter].toString()
        textViewResult.text = getMorseCode(alphabet[lastLetter])

        sound.setOnClickListener {
            if (isMuted) {
                sound.setImageResource(R.drawable.ic_unmute)
            } else {
                sound.setImageResource(R.drawable.ic_mute)
            }
            isMuted = !isMuted
        }

        home.setOnClickListener {
            finish()
        }

        buttonDot.setOnClickListener {
            val text = textViewResult.text
            if (lastSignal < text.length){
                var color = 0
                if (textViewResult.text[lastSignal].toString() == "•") {
                    color = green
                }
                else {
                    color = red
                }

                val coloredText = SpannableString(text.substring(0, lastSignal + 1))
                coloredText.setSpan(ForegroundColorSpan(green),0,lastSignal,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                coloredText.setSpan(ForegroundColorSpan(color),lastSignal,lastSignal + 1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                textViewResult.text = TextUtils.concat(coloredText, text.substring(lastSignal + 1))

                lastSignal++

                if (color == red) {
                    buttonMinus.isClickable = false
                    buttonDot.isClickable = false
                    Handler(Looper.getMainLooper()).postDelayed({
                        lastSignal = 0
                        val coloredText = SpannableString(text)
                        coloredText.setSpan(ForegroundColorSpan(white),0,text.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        textViewResult.text = coloredText
                        buttonMinus.isClickable = true
                        buttonDot.isClickable = true
                    }, 1000)
                }
                else {
                    if (!isMuted) {
                        val media = MediaPlayer.create(this, R.raw.dot)
                        mediaQueue.add(media)
                        playMediaQueue()
                    }
                }
                if (lastSignal == text.length) {
                    buttonMinus.isClickable = false
                    buttonDot.isClickable = false
                    Handler(Looper.getMainLooper()).postDelayed({
                        lastLetter++
                        if (alphabet[lastLetter] > 'Z') {
                            lastLetter = 0
                        }
                        textViewRandom.text = alphabet[lastLetter].toString()
                        textViewResult.text = getMorseCode(alphabet[lastLetter])
                        lastSignal = 0
                        buttonMinus.isClickable = true
                        buttonDot.isClickable = true
                    }, 1000)

                }

            }

        }

        buttonMinus.setOnClickListener {
            val text = textViewResult.text
            if (lastSignal < text.length){
                var color = 0
                if (textViewResult.text[lastSignal].toString() == "—") {
                    color = green
                }
                else {
                    color = red
                }

                val coloredText = SpannableString(text.substring(0, lastSignal + 1))
                coloredText.setSpan(ForegroundColorSpan(green),0,lastSignal,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                coloredText.setSpan(ForegroundColorSpan(color),lastSignal,lastSignal + 1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                textViewResult.text = TextUtils.concat(coloredText, text.substring(lastSignal + 1))

                lastSignal++

                if (color == red) {
                    buttonMinus.isClickable = false
                    buttonDot.isClickable = false
                    Handler(Looper.getMainLooper()).postDelayed({
                        lastSignal = 0
                        val coloredText = SpannableString(text)
                        coloredText.setSpan(ForegroundColorSpan(white),0,text.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        textViewResult.text = coloredText
                        buttonMinus.isClickable = true
                        buttonDot.isClickable = true
                    }, 1000)
                }
                else {
                    if (!isMuted) {
                        val media = MediaPlayer.create(this, R.raw.minus)
                        mediaQueue.add(media)
                        playMediaQueue()
                    }
                    if (lastSignal == text.length) {
                        buttonMinus.isClickable = false
                        buttonDot.isClickable = false
                        Handler(Looper.getMainLooper()).postDelayed({
                            lastLetter++
                            if (alphabet[lastLetter] > 'Z') {
                                lastLetter = 0
                            }
                            textViewRandom.text = alphabet[lastLetter].toString()
                            textViewResult.text = getMorseCode(alphabet[lastLetter])
                            lastSignal = 0
                            buttonMinus.isClickable = true
                            buttonDot.isClickable = true
                        }, 1000)

                    }
                }


            }


        }
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
