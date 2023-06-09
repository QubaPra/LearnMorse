package com.example.learnmorse

import android.content.Context
import android.content.SharedPreferences
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
import androidx.core.view.isInvisible

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
    private var lastChar = randomChar
    private var morseCode = getMorseCode(randomChar)

    lateinit var sharedPreferences: SharedPreferences

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
            textView.text = getString(R.string.testTextView)
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
            if (!isTestTutorial && textViewRandom.currentTextColor == getColor(R.color.white))
            {
                checkResult()
            }
        }
        layout.setOnLongClickListener {
            if (!isTestTutorial && textViewRandom.currentTextColor == getColor(R.color.white))
            {
                textViewResult.text = ""
            }
            true
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
                lastChar = randomChar
                randomChar = alphabet.random()
                while (randomChar == lastChar) {
                    randomChar = alphabet.random()
                }
                textViewRandom.text = randomChar.toString()
                textViewRandom.setTextColor(getColor(R.color.white))
                morseCode = getMorseCode(randomChar)
                buttonMinus.isClickable = true
                buttonDot.isClickable = true
                layout.isClickable = true
                countDownTimer.cancel()
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
        textView.text = getString(R.string.testInfoTextView)
        tutorialView.visibility = View.VISIBLE
        testView.visibility = View.GONE
        info.visibility = View.INVISIBLE

        countDownTimer = object : CountDownTimer(12000, 500) {
            override fun onTick(millisUntilFinished: Long) {
                when (((12000 - millisUntilFinished)*2)/1000/2.0) {
                    0.0 -> tutorialTextView.text=getString(R.string.testTutorial1)
                    3.0 -> blinkAnimation(buttonDot)
                    3.5 -> blinkAnimation(buttonMinus)
                    4.0 -> blinkAnimation(buttonDot)
                    4.5 -> blinkAnimation(buttonMinus)
                    6.5 -> tutorialTextView.text=getString(R.string.testTutorial2)
                    10.0 -> blinkAnimation(tutorialContent)
                    11.0 -> blinkAnimation(tutorialContent)
                }
            }

            override fun onFinish() {
                testView.visibility = View.VISIBLE
                tutorialView.visibility = View.GONE
                info.visibility = View.VISIBLE
                isTestTutorial = false
                textView.text = getString(R.string.testTextView)
                textViewRandom.text = randomChar.toString()
            }
        }.start()

    }
    override fun onPause() {
        super.onPause()
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        // Save values for lightSpeedSetting, learn_alphabet and train_alphabet to SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putBoolean("isTestTutorial", isTestTutorial)
        editor.apply()
    }
}