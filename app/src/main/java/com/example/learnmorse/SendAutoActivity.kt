package com.example.learnmorse

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

var isSendTutorial = true

class SendAutoActivity : AppCompatActivity() {

    private lateinit var countDownTimer: CountDownTimer
    var wiad: String = ""
    var isSignal = false

    private lateinit var cameraManager: CameraManager
    private var cameraId: String? = null

    private lateinit var textViewResult: TextView
    private lateinit var textView: TextView
    private lateinit var buttonStart: Button
    private lateinit var buttonDot: Button
    private lateinit var buttonMinus: Button
    private lateinit var buttonSpace: Button
    private lateinit var buttonBack: Button
    private lateinit var closeTutorial: ImageView
    private lateinit var tutorialView: ConstraintLayout
    private lateinit var sendView: ConstraintLayout
    private lateinit var info: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_auto)

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        try {
            cameraId = cameraManager.cameraIdList[0]
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

        info = findViewById(R.id.info)


        val home = findViewById<ImageView>(R.id.home)
        home.setOnClickListener {
            finish()
        }

        buttonMinus = findViewById(R.id.button_minus)
        buttonDot = findViewById(R.id.button_dot)
        buttonSpace = findViewById(R.id.button_space)
        textViewResult = findViewById(R.id.textview_result)
        textView = findViewById(R.id.textView)
        buttonBack = findViewById(R.id.button_backspace)
        buttonStart = findViewById(R.id.button_start)
        closeTutorial = findViewById(R.id.closeTutorial)
        tutorialView = findViewById(R.id.tutorial)
        sendView = findViewById(R.id.send)

        if (isSendTutorial){
            tutorialAnimation()
        }

        closeTutorial.setOnClickListener {
            countDownTimer.cancel()
            sendView.visibility = View.VISIBLE
            tutorialView.visibility = View.GONE
            info.visibility = View.VISIBLE
            isSendTutorial = false
            textView.text = "Wiadomość"
        }

        info.setOnClickListener {
            if (!isSendTutorial) {
                isSendTutorial = true
                tutorialAnimation()
            }
        }

        buttonMinus.setOnClickListener {
            if (textViewResult.text.toString().substringAfterLast(" ").length < 4 && !isSendTutorial) {
                textViewResult.append("—")
            }
        }
        buttonDot.setOnClickListener {
            if (textViewResult.text.toString().substringAfterLast(" ").length < 4 && !isSendTutorial) {
                textViewResult.append("•")
            }
        }
        buttonSpace.setOnClickListener {
            if (!isSendTutorial) {
                var text = textViewResult.text.toString()
                if (text.isNotEmpty()) {
                    if (!(text.length > 3 && text.substring(
                            text.length - 3,
                            text.length - 1
                        ) == ". ")
                    ) {
                        textViewResult.append(" ")
                        text = textViewResult.text.toString()

                        if (text.length > 3 && text.substring(
                                text.length - 3,
                                text.length - 1
                            ) == "  "
                        )

                            textViewResult.text = text.substring(0, text.length - 3) + ".  "
                        else {
                            var letter = getNormalLetter(lastLetter(text)) + " "
                            if (letter == "  " && lastLetter(text) != "") {
                                letter = " "
                            }
                            if (text.substringBeforeLast(" ")
                                    .substringBeforeLast(" ") == lastLetter(text)
                            ) {
                                textViewResult.text = letter
                            } else
                                textViewResult.text =
                                    text.substringBeforeLast(" ").substringBeforeLast(" ") + letter
                        }
                    }
                }
            }
        }

        buttonBack.setOnClickListener {
            if (!isSendTutorial) {
                val text = textViewResult.text.toString()
                if (text.isNotEmpty()) {
                    if (text[text.length - 1] == ' ' && text.length > 1) {
                        textViewResult.text = text.substring(0, text.length - 2) + " "
                    } else if (text.isNotEmpty()) {
                        textViewResult.text = text.substring(0, text.length - 1)
                    }
                }
                if (text.length > 3 && text.substring(text.length - 3, text.length - 1) == ". ") {
                    textViewResult.text = text.substring(0, text.length - 3) + "  "
                }
            }

        }
        buttonBack.setOnLongClickListener {
            if (!isSendTutorial) {
                textViewResult.text = ""
            }
            true
        }
        buttonStart.setOnClickListener {

            if (textViewResult.text.toString().isNotEmpty() && !isSendTutorial) {
                translate()
                if (buttonStart.text.toString() == "Start!") {
                    buttonStart.text = "Stop!"
                    isSignal = true
                    lightTimer()

                } else {
                    buttonStart.text = "Start!"
                    lightOff()
                    countDownTimer.cancel()
                }
            }
        }
    }

    private fun lastLetter(str: String): String {
        var letter = ""
        var i = str.length - 2
        while (i >= 0 && str[i] != ' ') {
            i--
        }
        letter = str.substring(i + 1, str.length - 1)
        return letter
    }

    private fun lightTimer() {

        var signalTime: Long = 0
        var wait = false

        if (wiad[0] == '•') {
            signalTime = 100
        } else if (wiad[0] == '—') {
            signalTime = 500
        } else if (wiad[0] == '|' || wiad[0] == ' ') {
            signalTime = 500
            wait = true
        }
        wiad = wiad.substring(1, wiad.length)


        countDownTimer = object : CountDownTimer(500, 100) {

            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                if (!wait) {
                    lightOn()
                }
                countDownTimer = object : CountDownTimer(signalTime, 100) {

                    override fun onTick(millisUntilFinished: Long) {
                    }

                    override fun onFinish() {
                        if (!wait) {
                            lightOff()
                        }
                        wait = false
                        if (wiad.isNotEmpty()) {
                            lightTimer()
                        } else {
                            isSignal = false
                            buttonStart.text = "Start!"
                            countDownTimer.cancel()
                        }
                    }
                }.start()
            }
        }.start()

    }

    private fun translate() {
        wiad = ""
        val text = textViewResult.text.toString()
        for (i in text.indices) {
            if (text[i] == ' ' || text[i] == '.') {
                wiad += " "
            } else {
                wiad += getMorseCode(text[i]) + "|"
            }
        }
    }

    private fun lightOn() {
        try {
            cameraManager.setTorchMode(cameraId!!, true)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun lightOff() {
        try {
            cameraManager.setTorchMode(cameraId!!, false)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isSignal) {
            lightOff()
            countDownTimer.cancel()
        }
        isSendTutorial = false

        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }

    private fun tutorialAnimation() {
        if (isSignal) {
            lightOff()
            countDownTimer.cancel()
        }
        val tutorialTextView = findViewById<TextView>(R.id.tutorialTextView)
        textView.text = "Instrukcja"
        tutorialView.visibility = View.VISIBLE
        sendView.visibility = View.GONE
        info.visibility = View.INVISIBLE

        countDownTimer = object : CountDownTimer(29000, 500) {
            override fun onTick(millisUntilFinished: Long) {
                when (((29000 - millisUntilFinished)*2)/1000/2.0) {
                    0.0 -> tutorialTextView.text="Nadaj wiadomość używając przycisków na dole!"
                    2.0 -> blinkAnimation(buttonDot)
                    2.5 -> blinkAnimation(buttonMinus)
                    3.0 -> blinkAnimation(buttonDot)
                    3.5 -> blinkAnimation(buttonMinus)
                    4.5 -> tutorialTextView.text="Aby zatwierdzić literę kliknij raz kreskę pionową!"
                    6.5 -> blinkAnimation(buttonSpace)
                    8.0 -> tutorialTextView.text="Następne naciśniecie kreski pionowej spowoduje zakończenie wyrazu!"
                    11.0 -> blinkAnimation(buttonSpace)
                    12.5 -> tutorialTextView.text="Trzecie naciśniecie kreski pionowej spowoduje zakończenie zdania!"
                    15.0 -> blinkAnimation(buttonSpace)
                    17.0 -> tutorialTextView.text="Naciśnij backspace aby zmazać ostatni znak!"
                    19.0 -> blinkAnimation(buttonBack)
                    21.0 -> tutorialTextView.text="Przytrzymaj backspace aby zmazać całą wiadomość!"
                    23.0 -> longBlinkAnimation(buttonBack)
                    25.5 -> tutorialTextView.text="Naciśnij Start! aby nadać wiadomość latarką!"
                    27.5 -> blinkAnimation(buttonStart)
                }
            }

            override fun onFinish() {
                sendView.visibility = View.VISIBLE
                tutorialView.visibility = View.GONE
                info.visibility = View.VISIBLE
                isSendTutorial = false
                textView.text = "Wiadomość"

            }
        }.start()

    }
}