package com.example.learnmorse

import android.content.res.Configuration
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
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat

var lastTrainLetter = 'A'
var lastTrainSignal = 0
var reps = 0
var learnMode = true

class TrainActivity : AppCompatActivity() {

    private lateinit var buttonDot: Button
    private lateinit var buttonMinus: Button
    private lateinit var textViewResult: TextView
    private lateinit var textViewRandom: TextView
    private lateinit var layout: RelativeLayout
    private lateinit var home: ImageView
    private lateinit var textView: TextView

    private var mediaPlayer: MediaPlayer? = null
    private val mediaQueue: MutableList<MediaPlayer> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train)

        val green = ContextCompat.getColor(this, R.color.accent1)
        val red = ContextCompat.getColor(this, R.color.accent2)
        val white = ContextCompat.getColor(this, R.color.white)
        buttonDot = findViewById(R.id.button_dot)
        buttonMinus = findViewById(R.id.button_minus)
        textViewResult = findViewById(R.id.textview_result)
        textViewRandom = findViewById(R.id.textview_random)
        layout = findViewById(R.id.content)
        home = findViewById(R.id.home)
        textView = findViewById(R.id.textView)
        val sound = findViewById<ImageView>(R.id.sound)
        if (!isMuted) {
            sound.setImageResource(R.drawable.ic_unmute)
        } else {
            sound.setImageResource(R.drawable.ic_mute)
        }

        // Change text of textViewRandom and textViewResult to 'A' in Morse code

        textViewRandom.text = lastTrainLetter.toString()
        if (learnMode) {
            textView.text = "Przepisz literę"
            textViewResult.text = getMorseCode(lastTrainLetter)

        } else {
            textView.text = "Odgadnij literę"
            textViewResult.text = ""
        }

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

            if (learnMode){
                val text = textViewResult.text
                if (lastTrainSignal < text.length){
                    var color = 0
                    if (textViewResult.text[lastTrainSignal].toString() == "•") {
                        color = green
                    }
                    else {
                        color = red
                    }

                    val coloredText = SpannableString(text.substring(0, lastTrainSignal + 1))
                    coloredText.setSpan(ForegroundColorSpan(green),0,lastTrainSignal,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    coloredText.setSpan(ForegroundColorSpan(color),lastTrainSignal,lastTrainSignal + 1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    textViewResult.text = TextUtils.concat(coloredText, text.substring(lastTrainSignal + 1))

                    lastTrainSignal++

                    if (color == red) {
                        buttonMinus.isClickable = false
                        buttonDot.isClickable = false
                        Handler(Looper.getMainLooper()).postDelayed({
                            lastTrainSignal = 0
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
                    if (lastTrainSignal == text.length) {
                        buttonMinus.isClickable = false
                        buttonDot.isClickable = false
                        Handler(Looper.getMainLooper()).postDelayed({
                            reps++
                            if (reps > 3) {
                                learnMode = false
                                textView.text = "Odgadnij literę"
                                reps = 0
                                lastTrainLetter-=3
                                textViewRandom.text = lastTrainLetter.toString()
                                textViewResult.text = ""
                                lastTrainSignal = 0
                                buttonMinus.isClickable = true
                                buttonDot.isClickable = true
                            }
                            else {
                                lastTrainLetter++
                                if (lastTrainLetter > 'Z') {
                                    lastTrainLetter = 'A'
                                }
                                textViewRandom.text = lastTrainLetter.toString()
                                textViewResult.text = getMorseCode(lastTrainLetter)
                                lastTrainSignal = 0
                                buttonMinus.isClickable = true
                                buttonDot.isClickable = true
                            }
                        }, 1000)

                    }

                }
            }
            else {
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


        }

        layout.setOnClickListener {
            if (!learnMode) {
                checkResult()
            }

        }

        buttonMinus.setOnClickListener {

            if (learnMode){
                val text = textViewResult.text
                if (lastTrainSignal < text.length){
                    var color = 0
                    if (textViewResult.text[lastTrainSignal].toString() == "—") {
                        color = green
                    }
                    else {
                        color = red
                    }

                    val coloredText = SpannableString(text.substring(0, lastTrainSignal + 1))
                    coloredText.setSpan(ForegroundColorSpan(green),0,lastTrainSignal,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    coloredText.setSpan(ForegroundColorSpan(color),lastTrainSignal,lastTrainSignal + 1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    textViewResult.text = TextUtils.concat(coloredText, text.substring(lastTrainSignal + 1))

                    lastTrainSignal++

                    if (color == red) {
                        buttonMinus.isClickable = false
                        buttonDot.isClickable = false
                        Handler(Looper.getMainLooper()).postDelayed({
                            lastTrainSignal = 0
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
                        if (lastTrainSignal == text.length) {
                            buttonMinus.isClickable = false
                            buttonDot.isClickable = false
                            Handler(Looper.getMainLooper()).postDelayed({
                                reps++
                                if (reps > 3) {
                                    learnMode = false
                                    textView.text = "Odgadnij literę"
                                    reps = 0
                                    lastTrainLetter-=3
                                    textViewRandom.text = lastTrainLetter.toString()
                                    textViewResult.text = ""
                                    lastTrainSignal = 0
                                    buttonMinus.isClickable = true
                                    buttonDot.isClickable = true
                                }
                                else {
                                    lastTrainLetter++
                                    if (lastTrainLetter > 'Z') {
                                        lastTrainLetter = 'A'
                                    }
                                    textViewRandom.text = lastTrainLetter.toString()
                                    textViewResult.text = getMorseCode(lastTrainLetter)
                                    lastTrainSignal = 0
                                    buttonMinus.isClickable = true
                                    buttonDot.isClickable = true
                                }

                            }, 1000)
                        }


                    }

                }
            }
            else {
                if (textViewResult.text.length < 4) {
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

        }
    }

    override fun onStop() {
        super.onStop()
        for (media in mediaQueue) {
            media.release()
        }
    }

    private fun checkResult() {
        val input = textViewResult.text.toString()
        if (input == getMorseCode(lastTrainLetter)) {
            textViewRandom.setTextColor(getColor(R.color.accent1))
            lastTrainLetter++
            reps++
        } else {
            textViewRandom.setTextColor(getColor(R.color.accent2))

        }
        buttonMinus.isClickable = false
        buttonDot.isClickable = false
        layout.isClickable = false
        Handler(Looper.getMainLooper()).postDelayed({

            if (reps > 3) {
                learnMode = true
                textView.text = "Przepisz literę"
                reps = 0
                if (lastTrainLetter > 'Z') {
                    lastTrainLetter = 'A'
                }
                textViewResult.text = getMorseCode(lastTrainLetter)
                lastTrainSignal = 0
            }
            else {
                textViewResult.text = ""
            }
            textViewRandom.text = lastTrainLetter.toString()
            textViewRandom.setTextColor(getColor(R.color.white))
            buttonMinus.isClickable = true
            buttonDot.isClickable = true
            layout.isClickable = true

        }, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        lastTrainSignal = 0
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
