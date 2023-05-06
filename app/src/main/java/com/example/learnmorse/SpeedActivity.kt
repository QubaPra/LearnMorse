package com.example.learnmorse

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.IEEErem
import kotlin.math.roundToInt

var bestScore = "0 pkt"
var score = "0 pkt"
var isSpeedTutorial = true
class SpeedActivity : AppCompatActivity() {
    private lateinit var buttonDot: Button
    private lateinit var buttonMinus: Button
    private lateinit var textViewResult: TextView
    private lateinit var textViewRandom: TextView
    private lateinit var textView: TextView
    private lateinit var layout: RelativeLayout
    private lateinit var home: ImageView

    private lateinit var tutorialView: ConstraintLayout
    private lateinit var speedLayout: ConstraintLayout
    private lateinit var closeTutorial: ImageView

    private var randomChar = alphabet.random()
    private var morseCode = getMorseCode(randomChar)

    private lateinit var countdownTextView: TextView
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var animationTimer: CountDownTimer
    private var timeLeft: Long = 20000
    private var addTimeAmount: Long = 1500
    private var subtractTimeAmount: Long = 500

    lateinit var sharedPreferences: SharedPreferences

    private var result = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speed)

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        timeLeft= 20000
        addTimeAmount = 1500
        subtractTimeAmount = 500

        tutorialView = findViewById(R.id.tutorial)
        speedLayout = findViewById(R.id.speedlayout)

        buttonDot = findViewById(R.id.button_dot)
        buttonMinus = findViewById(R.id.button_minus)
        textViewResult = findViewById(R.id.textview_result)
        textViewRandom = findViewById(R.id.textview_random)
        textView = findViewById(R.id.textView)
        layout = findViewById(R.id.content)
        home = findViewById(R.id.home)
        closeTutorial = findViewById(R.id.closeTutorial)

        countdownTextView = findViewById(R.id.time)

        home.setOnClickListener {
            finish()
        }

        if (isSpeedTutorial) {
            tutorialAnimation()

        } else {
            timer()
            textViewRandom.text = randomChar.toString()
        }
        closeTutorial.setOnClickListener {
            animationTimer.cancel()
            speedLayout.visibility = View.VISIBLE
            tutorialView.visibility = View.GONE
            isSpeedTutorial = false
            textViewRandom.text = randomChar.toString()
            timer()
        }


        buttonDot.setOnClickListener {
            if (!isSpeedTutorial) {
                if (textViewResult.text.length < 4) {
                    textViewResult.append("•")

                } else {
                    checkResult()
                }
            }
        }
        buttonMinus.setOnClickListener {
            if (!isSpeedTutorial) {
                if (textViewResult.text.length < 4) {
                    textViewResult.append("—")
                } else {
                    checkResult()
                }
            }

        }

        layout.setOnClickListener {
            if (!isSpeedTutorial)
            {
                checkResult()
            }
        }
    }

    private fun checkResult() {
        if (timeLeft>0){
            val input = textViewResult.text.toString()

            if (input == morseCode) {
                textViewRandom.setTextColor(getColor(R.color.accent1))
                addTime()
                result++
                textView.text = "$result pkt"
            } else {
                textViewRandom.setTextColor(getColor(R.color.accent2))
                subtractTime()
            }
            buttonMinus.isClickable = false
            buttonDot.isClickable = false
            layout.isClickable = false
            Handler(Looper.getMainLooper()).postDelayed({
                textViewResult.text = ""
                randomChar = alphabet.random()
                textViewRandom.text = randomChar.toString()

                textViewRandom.setTextColor(getColor(R.color.white))

                morseCode = getMorseCode(randomChar)
                buttonMinus.isClickable = true
                buttonDot.isClickable = true
                layout.isClickable = true
            }, 100)
        }
    }

    private fun timer() {
        countDownTimer = object : CountDownTimer(timeLeft, 100) {

            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = ((millisUntilFinished / 1000)).toString()
                val millisecondsLeft = (millisUntilFinished % 1000 / 100).toString()
                countdownTextView.text = "$secondsLeft.$millisecondsLeft"
            }

            override fun onFinish() {
                countdownTextView.text = "Koniec!"
                buttonMinus.isClickable = false
                buttonDot.isClickable = false
                layout.isClickable = false
                score = textView.text.toString()
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this@SpeedActivity, SpeedEndActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 500)
            }
        }.start()
    }

    private fun addTime() {
        if (countdownTextView.text != "Koniec!")
        {
            timeLeft = (countdownTextView.text.toString().toDouble() * 1000).toLong()
        }
        timeLeft += addTimeAmount
        countDownTimer.cancel()
        if (timeLeft > 0 && countdownTextView.text != "Koniec!") {
            timer()
        } else {
            countdownTextView.text = "Koniec!"
            buttonMinus.isClickable = false
            buttonDot.isClickable = false
            layout.isClickable = false
            score = textView.text.toString()
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, SpeedEndActivity::class.java)
                startActivity(intent)
                finish()
            }, 500)
        }
    }

    private fun subtractTime() {
        if (countdownTextView.text != "Koniec!")
        {
            timeLeft = (countdownTextView.text.toString().toDouble() * 1000).toLong()
        }
        timeLeft -= subtractTimeAmount
        countDownTimer.cancel()
        if (timeLeft > 0 && countdownTextView.text != "Koniec!") {
            timer()
        } else {
            countdownTextView.text = "Koniec!"
            buttonMinus.isClickable = false
            buttonDot.isClickable = false
            layout.isClickable = false
            score = textView.text.toString()
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, SpeedEndActivity::class.java)
                startActivity(intent)
                finish()
            }, 500)
        }
    }




    private fun tutorialAnimation() {

        val tutorialTextView = findViewById<TextView>(R.id.tutorialTextView)
        val tutorialContent = findViewById<RelativeLayout>(R.id.tutorialContent)
        val timeBackground = findViewById<RelativeLayout>(R.id.timeBackground)
        val textView = findViewById<TextView>(R.id.textView)
        tutorialView.visibility = View.VISIBLE
        speedLayout.visibility = View.GONE

        animationTimer = object : CountDownTimer(14500, 500) {
            override fun onTick(millisUntilFinished: Long) {
                when (((14500 - millisUntilFinished)*2)/1000/2.0) {
                    0.0 -> tutorialTextView.text="Odgadnij literę używając przycisków na dole!"
                    2.0 -> blinkAnimation(buttonDot)
                    2.5 -> blinkAnimation(buttonMinus)
                    3.0 -> blinkAnimation(buttonDot)
                    3.5 -> blinkAnimation(buttonMinus)
                    4.5 -> tutorialTextView.text="Aby sprawdzić poprawność sygnału naciśnij tutaj!"
                    6.0 -> blinkAnimation(tutorialContent)
                    6.5 -> blinkAnimation(tutorialContent)
                    7.5 -> tutorialTextView.text="Masz 20 sekund na zdobycie jak największej ilości punktów!"
                    9.0 -> blinkAnimation(timeBackground)
                    9.5 -> blinkAnimation(textView)
                    10.5 -> tutorialTextView.text="Za każdą poprawną odpowiedź zyskujesz punkt i czas, za błędną go tracisz!"
                }
            }

            override fun onFinish() {
                speedLayout.visibility = View.VISIBLE
                tutorialView.visibility = View.GONE
                isSpeedTutorial = false
                textViewRandom.text = randomChar.toString()
                timer()
            }
        }.start()
    }
    override fun onDestroy() {
        super.onDestroy()
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
        if (::animationTimer.isInitialized) {
            animationTimer.cancel()
        }
        isSpeedTutorial = false
        lastSignal = 0
        finish()
    }
    override fun onBackPressed() {
        isSpeedTutorial = false
        val intent = Intent(this, SpeedStartActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        finish()
    }
    override fun onPause() {
        super.onPause()

        // Save values for lightSpeedSetting, learn_alphabet and train_alphabet to SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString("score", score)
        editor.putString("bestScore", bestScore)
        editor.putBoolean("isSpeedTutorial", isSpeedTutorial)
        editor.apply()
    }
}