package com.example.learnmorse

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class SendAutoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_auto)

        val home = findViewById<ImageView>(R.id.home)
        home.setOnClickListener {
            navigateUpTo(Intent(baseContext, MainActivity::class.java))
        }

        val buttonMinus = findViewById<Button>(R.id.button_minus)
        val buttonDot = findViewById<Button>(R.id.button_dot)
        val buttonSpace = findViewById<Button>(R.id.button_space)
        val textView = findViewById<TextView>(R.id.textview_result)
        val buttonBack = findViewById<Button>(R.id.button_back)
        val buttonStart = findViewById<Button>(R.id.button_start)

        buttonMinus.setOnClickListener {
            textView.append("—")
        }
        buttonDot.setOnClickListener {
            textView.append("•")
        }
        buttonSpace.setOnClickListener {
            var text = textView.text.toString()
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

    }


    fun lastLetter(str: String): String {
        var letter = ""
        var i = str.length - 2
        while (i>=0 && str[i] != ' ') {
            i--
        }
        letter = str.substring(i+1, str.length-1)
        return letter
    }

}