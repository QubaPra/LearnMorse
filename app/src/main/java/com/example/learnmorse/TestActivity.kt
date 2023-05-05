package com.example.learnmorse

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

var isTestTutorial = true

class TestActivity : AppCompatActivity() {

    private lateinit var buttonDot: Button
    private lateinit var buttonMinus: Button
    private lateinit var textViewResult: TextView
    private lateinit var textViewRandom: TextView
    private lateinit var layout: RelativeLayout
    private lateinit var home: ImageView
    private lateinit var textView: TextView
    private lateinit var info: ImageView

    private lateinit var tutorialView: ConstraintLayout
    private lateinit var testView: ConstraintLayout
    private lateinit var closeTutorial: ImageView

    private var mediaPlayer: MediaPlayer? = null
    private val mediaQueue: MutableList<MediaPlayer> = mutableListOf()

    private var randomChar = alphabet.random()
    private var morseCode = getMorseCode(randomChar)

    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_activity)

        buttonDot = findViewById(R.id.button_dot)
        buttonMinus = findViewById(R.id.button_minus)
        textViewResult = findViewById(R.id.textview_result)
        textViewRandom = findViewById(R.id.textview_random)
        layout = findViewById(R.id.content)
        home = findViewById(R.id.home)
        textView = findViewById(R.id.textView)
        info = findViewById(R.id.info)
        tutorialView = findViewById(R.id.tutorial)
        testView = findViewById(R.id.test)
        closeTutorial = findViewById(R.id.closeTutorial)

        val sound = findViewById<ImageView>(R.id.sound)
        if (!isMuted) {
            sound.setImageResource(R.drawable.ic_unmute)
        } else {
            sound.setImageResource(R.drawable.ic_mute)
        }

        home.setOnClickListener {
            finish()
        }

        if (isTestTutorial) {
            tutorialAnimation()
        }
        else {
            textViewRandom.text = randomChar.toString()
        }

        closeTutorial.setOnClickListener {
            countDownTimer.cancel()
            testView.visibility = View.VISIBLE
            tutorialView.visibility = View.GONE
            info.visibility = View.VISIBLE
            isTestTutorial = false
            textView.text = "Odgadnij literę"
            textViewRandom.text = randomChar.toString()
        }

            info.setOnClickListener {
                if (!isTestTutorial) {
                    isTestTutorial = true
                    tutorialAnimation()
                }
            }

            sound.setOnClickListener {
                if (!isTestTutorial) {
                    if (isMuted) {
                        sound.setImageResource(R.drawable.ic_unmute)
                    } else {
                        sound.setImageResource(R.drawable.ic_mute)
                    }
                    isMuted = !isMuted
                }
            }



            buttonDot.setOnClickListener {
                if (!isTestTutorial) {
                    if (textViewResult.text.length < 4) {
                        textViewResult.append("•")
                        if (!isMuted) {
                            val media = MediaPlayer.create(this, R.raw.dot)
                            mediaQueue.add(media)
                            playMediaQueue()
                        }
                    } else {
                        checkResult()
                    }
                }
            }
            buttonMinus.setOnClickListener {
                if (!isTestTutorial) {
                    if (textViewResult.text.length < 4) {
                        textViewResult.append("—")
                        if (!isMuted) {
                            val media = MediaPlayer.create(this, R.raw.minus)
                            mediaQueue.add(media)
                            playMediaQueue()
                        }
                    } else {
                        checkResult()
                    }
                }

            }

            layout.setOnClickListener {
                if (!isTestTutorial)
                {
                    checkResult()
                }
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

        countDownTimer = object : CountDownTimer(1000,1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                textViewResult.text = ""
                randomChar = alphabet.random()
                textViewRandom.text = randomChar.toString()
                textViewRandom.setTextColor(getColor(R.color.white))
                morseCode = getMorseCode(randomChar)
                buttonMinus.isClickable = true
                buttonDot.isClickable = true
                layout.isClickable = true
            }
        }.start()
    }

    override fun onStop() {
        super.onStop()
        for (media in mediaQueue) {
            media.release()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isTestTutorial)
        {
            isTestTutorial = false
        }
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
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
    private fun tutorialAnimation() {
        val tutorialTextView = findViewById<TextView>(R.id.tutorialTextView)
        val tutorialContent = findViewById<RelativeLayout>(R.id.tutorialContent)
        textView.text = "Instrukcja"
        tutorialView.visibility = View.VISIBLE
        testView.visibility = View.GONE
        info.visibility = View.INVISIBLE

        countDownTimer = object : CountDownTimer(8000, 500) {
            override fun onTick(millisUntilFinished: Long) {
                when (((8000 - millisUntilFinished)*2)/1000/2.0) {
                    0.0 -> tutorialTextView.text="Odgadnij literę używając przycisków na dole!"
                    2.0 -> blinkAnimation(buttonDot)
                    2.5 -> blinkAnimation(buttonMinus)
                    3.0 -> blinkAnimation(buttonDot)
                    3.5 -> blinkAnimation(buttonMinus)
                    4.5 -> tutorialTextView.text="Aby sprawdzić poprawność sygnału naciśnij tutaj!"
                    6.0 -> blinkAnimation(tutorialContent)
                    6.5 -> blinkAnimation(tutorialContent)
                }
            }

            override fun onFinish() {
                testView.visibility = View.VISIBLE
                tutorialView.visibility = View.GONE
                info.visibility = View.VISIBLE
                isTestTutorial = false
                textView.text = "Odgadnij literę"
                textViewRandom.text = randomChar.toString()
            }
        }.start()

    }
}