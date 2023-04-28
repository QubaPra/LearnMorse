package com.example.learnmorse

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TestActivity : AppCompatActivity() {


    private lateinit var buttonDot: Button
    private lateinit var buttonMinus: Button
    private lateinit var textViewResult: TextView
    private lateinit var textViewRandom: TextView
    private lateinit var layout: RelativeLayout

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

        textViewRandom.text = randomChar.toString()

        buttonDot.setOnClickListener {
            textViewResult.append(".")
        }

        buttonMinus.setOnClickListener {
            textViewResult.append("-")
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
        Handler().postDelayed({
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
        }, 1000)
    }
}
