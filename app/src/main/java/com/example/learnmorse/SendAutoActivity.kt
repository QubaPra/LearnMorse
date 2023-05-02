package com.example.learnmorse

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class SendAutoActivity : AppCompatActivity() {

    private lateinit var countDownTimer: CountDownTimer
    var wiad:String = ""
    var isSignal = false

    private lateinit var cameraManager: CameraManager
    private var cameraId: String? = null

    private lateinit var textView: TextView
    private lateinit var buttonStart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_auto)

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        try {
            cameraId = cameraManager.cameraIdList[0]
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }


        val home = findViewById<ImageView>(R.id.home)
        home.setOnClickListener {
            finish()
        }

        val buttonMinus = findViewById<Button>(R.id.button_minus)
        val buttonDot = findViewById<Button>(R.id.button_dot)
        val buttonSpace = findViewById<Button>(R.id.button_space)
        textView = findViewById(R.id.textview_result)
        val buttonBack = findViewById<Button>(R.id.button_backspace)
        buttonStart = findViewById(R.id.button_start)

        buttonMinus.setOnClickListener {
            if (textView.text.toString().substringAfterLast(" ").length<4) {
                textView.append("—")
            }
        }
        buttonDot.setOnClickListener {
            if (textView.text.toString().substringAfterLast(" ").length<4) {
                textView.append("•")
            }
        }
        buttonSpace.setOnClickListener {
            var text = textView.text.toString()
            if (text.isNotEmpty()) {
                if (!(text.length>3 && text.substring(text.length-3, text.length-1)==". ")) {
                    textView.append(" ")
                    text = textView.text.toString()

                    if (text.length>3 && text.substring(text.length-3, text.length-1)=="  " ) {

                        textView.text = text.substring(0, text.length-3)+".  "
                    }
                    else {
                        var letter = getNormalLetter(lastLetter(text)) + " "
                        if (letter == "  " && lastLetter(text) != "") {
                            letter = " "
                        }
                        if (letter != null) {
                            if (text.substringBeforeLast(" ")
                                    .substringBeforeLast(" ") == lastLetter(text)
                            ) {
                                textView.text = letter
                            } else
                                textView.text =
                                    text.substringBeforeLast(" ").substringBeforeLast(" ") + letter
                        }
                    }
                }
            }
        }

        buttonBack.setOnClickListener {
            var text = textView.text.toString()
            if (text.isNotEmpty()) {
                if (text[text.length-1]==' '&&text.length>1) {
                    textView.text = text.substring(0, text.length - 2)+" "
                }

                else if (text.isNotEmpty()) {
                    textView.text = text.substring(0, text.length - 1)
                }
            }
            if (text.length>3 && text.substring(text.length-3, text.length-1)==". ") {
                textView.text = text.substring(0, text.length - 3)+"  "
            }



        }
        buttonBack.setOnLongClickListener {
            textView.text = ""
            true
        }
        buttonStart.setOnClickListener {

            if (textView.text.toString().isNotEmpty()) {
                translate()
                if (buttonStart.text.toString() == "Start!") {
                    buttonStart.text = "Stop!"
                    isSignal = true
                    lightTimer()

                }
                else {
                    buttonStart.text = "Start!"
                    countDownTimer.cancel()
                }
            }
        }

    }


    private fun lastLetter(str: String): String {
        var letter = ""
        var i = str.length - 2
        while (i>=0 && str[i] != ' ') {
            i--
        }
        letter = str.substring(i+1, str.length-1)
        return letter
    }

    private fun lightTimer() {

        var signalTime: Long = 0
        var wait = false

        if (wiad[0] == '•') {
            signalTime = 100
        }
        else if (wiad[0] == '—') {
            signalTime = 500
        }
        else if (wiad[0] == '|' || wiad[0] == ' ') {
            signalTime = 500
            wait = true
        }
        wiad = wiad.substring(1, wiad.length)

        Handler(Looper.getMainLooper()).postDelayed({
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
                    }
                    else
                    {
                        isSignal = false
                        buttonStart.text = "Start!"
                        countDownTimer.cancel()
                    }
                }
            }.start()
        }, 500)
    }

    private fun translate() {
        wiad = ""
        val text = textView.text.toString()
        for (i in 0..text.length-1) {
            if (text[i] == ' ' || text[i] == '.')
            {
                wiad += " "
            }
            else
            {
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
}