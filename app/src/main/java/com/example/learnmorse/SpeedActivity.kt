package com.example.learnmorse

import android.content.Intent
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

    private var randomChar = alphabet.random()
    private var morseCode = getMorseCode(randomChar)

    private lateinit var countdownTextView: TextView
    private lateinit var countDownTimer: CountDownTimer
    private var timeLeft: Long = 20000
    private var addTimeAmount: Long = 1500
    private var subtractTimeAmount: Long = 500

    private var result = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speed)

        timeLeft= 20000
        addTimeAmount = 1500
        subtractTimeAmount = 500

        buttonDot = findViewById(R.id.button_dot)
        buttonMinus = findViewById(R.id.button_minus)
        textViewResult = findViewById(R.id.textview_result)
        textViewRandom = findViewById(R.id.textview_random)
        textView = findViewById(R.id.textView)
        layout = findViewById(R.id.content)
        home = findViewById(R.id.home)

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
        if (!isSpeedEnd) {
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
    }

    private fun addTime() {
        if (countdownTextView.text != "Koniec!")
        {
            timeLeft = (countdownTextView.text.toString().toDouble() * 1000).toLong()
        }
        timeLeft += addTimeAmount
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
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
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
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


    override fun onDestroy() {
        super.onDestroy()
        if (!isSpeedTutorial) {
            if (::countDownTimer.isInitialized) {
                countDownTimer.cancel()
            }
        }
        isSpeedTutorial = false
        lastSignal = 0
        finish()
    }
    override fun onBackPressed() {
        val intent = Intent(this, SpeedStartActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        finish()
    }

    private fun tutorialAnimation() {
        val tutorialView = findViewById<ConstraintLayout>(R.id.tutorial)
        val speedLayout = findViewById<ConstraintLayout>(R.id.speedlayout)
        val tutorialTextView = findViewById<TextView>(R.id.tutorialTextView)
        val tutorialContent = findViewById<RelativeLayout>(R.id.tutorialContent)
        val timeBackground = findViewById<RelativeLayout>(R.id.timeBackground)
        val textView = findViewById<TextView>(R.id.textView)
        tutorialView.visibility = View.VISIBLE
        speedLayout.visibility = View.GONE
        Handler(Looper.getMainLooper()).postDelayed({
            blinkAnimation(buttonDot)
            Handler(Looper.getMainLooper()).postDelayed({
                blinkAnimation(buttonMinus)
                Handler(Looper.getMainLooper()).postDelayed({
                    blinkAnimation(buttonDot)
                    Handler(Looper.getMainLooper()).postDelayed({
                        blinkAnimation(buttonMinus)
                        Handler(Looper.getMainLooper()).postDelayed({
                            tutorialTextView.text="Aby sprawdzić poprawność sygnału naciśnij tutaj!"
                            Handler(Looper.getMainLooper()).postDelayed({
                                blinkAnimation(tutorialContent)
                                Handler(Looper.getMainLooper()).postDelayed({
                                    blinkAnimation(tutorialContent)
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        tutorialTextView.text="Masz 20 sekund na zdobycie jak największej ilości punktów!"
                                        Handler(Looper.getMainLooper()).postDelayed({
                                            blinkAnimation(timeBackground)
                                            Handler(Looper.getMainLooper()).postDelayed({
                                                blinkAnimation(textView)
                                                Handler(Looper.getMainLooper()).postDelayed({
                                                    tutorialTextView.text="Za każdą poprawną odpowiedź zyskujesz punkt i czas, za błędną go tracisz!"
                                                    Handler(Looper.getMainLooper()).postDelayed({
                                                        speedLayout.visibility = View.VISIBLE
                                                        tutorialView.visibility = View.GONE
                                                        isSpeedTutorial = false
                                                        textViewRandom.text = randomChar.toString()
                                                        timer()
                                                    }, 4000)
                                                }, 2000)
                                            }, 500)

                                        }, 500)

                                    }, 2000)
                                }, 500)
                            }, 500)
                        }, 2000)
                    }, 500)
                }, 500)
            }, 500)
        }, 1000)
    }
}