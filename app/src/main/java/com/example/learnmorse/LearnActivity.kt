package com.example.learnmorse

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

var lastLetter = 0
var lastSignal = 0
var isMuted = false
var isLearnTutorial = true

class LearnActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private val mediaQueue: MutableList<MediaPlayer> = mutableListOf()

    private lateinit var buttonDot: Button
    private lateinit var buttonMinus: Button
    private lateinit var textViewResult: TextView
    private lateinit var textViewRandom: TextView
    private lateinit var home: ImageView
    private lateinit var textView: TextView
    private lateinit var info: ImageView

    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)

        val green = ContextCompat.getColor(this, R.color.accent1)
        val red = ContextCompat.getColor(this, R.color.accent2)
        val white = ContextCompat.getColor(this, R.color.white)
        textViewRandom = findViewById(R.id.textview_random)
        textViewResult = findViewById(R.id.textview_result)
        buttonDot = findViewById(R.id.button_dot)
        buttonMinus = findViewById(R.id.button_minus)
        home = findViewById(R.id.home)
        val sound = findViewById<ImageView>(R.id.sound)
        info = findViewById(R.id.info)
        textView = findViewById(R.id.textView)
        info.setOnClickListener {
            if (!isLearnTutorial) {
                isLearnTutorial = true
                tutorialAnimation()
            }
        }
        if (!isMuted) {
            sound.setImageResource(R.drawable.ic_unmute)
        } else {
            sound.setImageResource(R.drawable.ic_mute)
        }
        if (isLearnTutorial){
            tutorialAnimation()
        }
        else {
            textViewRandom.text = alphabet[lastLetter].toString()
            textViewResult.text = getMorseCode(alphabet[lastLetter])
        }



        sound.setOnClickListener {
            if (!isLearnTutorial) {
                if (isMuted) {
                    sound.setImageResource(R.drawable.ic_unmute)
                } else {
                    sound.setImageResource(R.drawable.ic_mute)
                }
                isMuted = !isMuted
            }
        }

        home.setOnClickListener {
            finish()
        }


        buttonDot.setOnClickListener {
            if (!isLearnTutorial) {
                val text = textViewResult.text
                if (lastSignal < text.length) {
                    var color = 0
                    if (textViewResult.text[lastSignal].toString() == "•") {
                        color = green
                    } else {
                        color = red
                    }

                    val coloredText = SpannableString(text.substring(0, lastSignal + 1))
                    coloredText.setSpan(
                        ForegroundColorSpan(green),
                        0,
                        lastSignal,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    coloredText.setSpan(
                        ForegroundColorSpan(color),
                        lastSignal,
                        lastSignal + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    textViewResult.text =
                        TextUtils.concat(coloredText, text.substring(lastSignal + 1))

                    lastSignal++

                    if (color == red) {
                        buttonMinus.isClickable = false
                        buttonDot.isClickable = false

                        countDownTimer = object : CountDownTimer(1000, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                            }

                            override fun onFinish() {
                                lastSignal = 0
                                val coloredText = SpannableString(text)
                                coloredText.setSpan(
                                    ForegroundColorSpan(white),
                                    0,
                                    text.length,
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                                textViewResult.text = coloredText
                                buttonMinus.isClickable = true
                                buttonDot.isClickable = true
                            }
                        }.start()

                    } else {
                        if (!isMuted) {
                            val media = MediaPlayer.create(this, R.raw.dot)
                            mediaQueue.add(media)
                            playMediaQueue()
                        }
                        if (lastSignal == text.length) {
                            buttonMinus.isClickable = false
                            buttonDot.isClickable = false

                            countDownTimer = object : CountDownTimer(1000, 1000) {
                                override fun onTick(millisUntilFinished: Long) {
                                }

                                override fun onFinish() {
                                    lastLetter++
                                    if (lastLetter > alphabet.length - 1) {
                                        lastLetter = 0
                                    }
                                    textViewRandom.text = alphabet[lastLetter].toString()
                                    textViewResult.text = getMorseCode(alphabet[lastLetter])
                                    lastSignal = 0
                                    buttonMinus.isClickable = true
                                    buttonDot.isClickable = true
                                }
                            }.start()
                        }

                    }

                }
            }

        }

        buttonMinus.setOnClickListener {
            if (!isLearnTutorial) {
                val text = textViewResult.text
                if (lastSignal < text.length) {
                    var color = 0
                    if (textViewResult.text[lastSignal].toString() == "—") {
                        color = green
                    } else {
                        color = red
                    }

                    val coloredText = SpannableString(text.substring(0, lastSignal + 1))
                    coloredText.setSpan(
                        ForegroundColorSpan(green),
                        0,
                        lastSignal,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    coloredText.setSpan(
                        ForegroundColorSpan(color),
                        lastSignal,
                        lastSignal + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    textViewResult.text =
                        TextUtils.concat(coloredText, text.substring(lastSignal + 1))

                    lastSignal++

                    if (color == red) {
                        buttonMinus.isClickable = false
                        buttonDot.isClickable = false

                        countDownTimer = object : CountDownTimer(1000, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                            }

                            override fun onFinish() {
                                lastSignal = 0
                                val coloredText = SpannableString(text)
                                coloredText.setSpan(
                                    ForegroundColorSpan(white),
                                    0,
                                    text.length,
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                                textViewResult.text = coloredText
                                buttonMinus.isClickable = true
                                buttonDot.isClickable = true
                            }
                        }.start()

                    } else {
                        if (!isMuted) {
                            val media = MediaPlayer.create(this, R.raw.minus)
                            mediaQueue.add(media)
                            playMediaQueue()
                        }
                        if (lastSignal == text.length) {
                            buttonMinus.isClickable = false
                            buttonDot.isClickable = false

                            countDownTimer = object : CountDownTimer(1000, 1000) {
                                override fun onTick(millisUntilFinished: Long) {
                                }

                                override fun onFinish() {
                                    lastLetter++
                                    if (lastLetter > alphabet.length - 1) {
                                        lastLetter = 0
                                    }
                                    textViewRandom.text = alphabet[lastLetter].toString()
                                    textViewResult.text = getMorseCode(alphabet[lastLetter])
                                    lastSignal = 0
                                    buttonMinus.isClickable = true
                                    buttonDot.isClickable = true
                                }
                            }.start()
                        }
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
        if (isLearnTutorial)
        {
            isLearnTutorial = false
        }
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
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
    private fun tutorialAnimation() {
        lastSignal = 0
        val tutorialView = findViewById<ConstraintLayout>(R.id.tutorial)
        val learnView = findViewById<ConstraintLayout>(R.id.learn)
        textView.text = "Instrukcja"
        tutorialView.visibility = View.VISIBLE
        learnView.visibility = View.GONE
        info.visibility = View.INVISIBLE

        countDownTimer = object : CountDownTimer(5000, 500) {
            override fun onTick(millisUntilFinished: Long) {
                when (((5000 - millisUntilFinished)*2)/1000/2.0) {
                    2.0 -> blinkAnimation(buttonDot)
                    2.5 -> blinkAnimation(buttonMinus)
                    3.0 -> blinkAnimation(buttonDot)
                    3.5 -> blinkAnimation(buttonMinus)
                }
            }

            override fun onFinish() {
                learnView.visibility = View.VISIBLE
                tutorialView.visibility = View.GONE
                info.visibility = View.VISIBLE
                isLearnTutorial = false
                textView.text = "Przepisz literę"
                // Change text of textViewRandom and textViewResult to 'A' in Morse code
                textViewRandom.text = alphabet[lastLetter].toString()
                textViewResult.text = getMorseCode(alphabet[lastLetter])
            }
        }.start()

    }

}
