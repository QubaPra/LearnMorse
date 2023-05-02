package com.example.learnmorse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

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

        buttonDot = findViewById(R.id.button_dot)
        buttonMinus = findViewById(R.id.button_minus)
        textViewResult = findViewById(R.id.textview_result)
        textViewRandom = findViewById(R.id.textview_random)
        textView = findViewById(R.id.textView)
        layout = findViewById(R.id.content)
        home = findViewById(R.id.home)

        countdownTextView = findViewById(R.id.time)

        timer()


        home.setOnClickListener {
            finish()
        }

        textViewRandom.text = randomChar.toString()

        buttonDot.setOnClickListener {
            if (textViewResult.text.length < 4) {
                textViewResult.append("•")

            }
            else {
                checkResult()
            }
        }
        buttonMinus.setOnClickListener {
            if (textViewResult.text.length < 4){
                textViewResult.append("—")
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
            }
        }.start()
    }
    private fun addTime() {
        timeLeft=(countdownTextView.text.toString().toDouble()*1000).toLong()
        timeLeft+= addTimeAmount
        countDownTimer.cancel()
        if (timeLeft > 0 && countdownTextView.text != "Koniec!") {
            timer()
        }
        else {
            countdownTextView.text = "Koniec!"

        }


    }
    private fun subtractTime() {
        timeLeft=(countdownTextView.text.toString().toDouble()*1000).toLong()
        timeLeft-= subtractTimeAmount
        countDownTimer.cancel()
        if (timeLeft > 0 && countdownTextView.text != "Koniec!") {
            timer()
        }
        else {
            countdownTextView.text = "Koniec!"
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        lastSignal = 0
    }
}