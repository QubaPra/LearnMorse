package com.example.learnmorse

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainSettingsActivity : AppCompatActivity() {

    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_settings)

        val closeButton: ImageView = findViewById(R.id.close)
        closeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }

        val send_toogle: RadioGroup = findViewById(R.id.send_toogle)
        val trening_toogle: RadioGroup = findViewById(R.id.trening_toogle)
        val nauka_toogle: RadioGroup = findViewById(R.id.nauka_toogle)

        if (learn_alphabet == "ABCDEFGHIJKLMNOPRSTUWYZ") nauka_toogle.check(R.id.nauka_on)
        else nauka_toogle.check(R.id.nauka_off)

        nauka_toogle.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.nauka_on -> {
                    learn_alphabet = "ABCDEFGHIJKLMNOPRSTUWYZ"
                }
                R.id.nauka_off -> {
                    learn_alphabet = shuffle(learn_alphabet)

                }
            }
            lastLetter = 0
            lastSignal = 0
        }

        if (train_alphabet == "ABCDEFGHIJKLMNOPRSTUWYZ") trening_toogle.check(R.id.trening_on)
        else trening_toogle.check(R.id.trening_off)

        trening_toogle.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.trening_on -> {
                    train_alphabet = "ABCDEFGHIJKLMNOPRSTUWYZ"
                }
                R.id.trening_off -> {
                    train_alphabet = shuffle(train_alphabet)
                }
            }
            last = 0
            lastTrainSignal = 0
            reps = 0
            tries = 0
            learnMode = true
        }

        if (lightSpeed == 1.0) send_toogle.check(R.id.send_normal)
        else if (lightSpeed == 3.0) send_toogle.check(R.id.send_slow)
        else if (lightSpeed == 0.5) send_toogle.check(R.id.send_fast)

        send_toogle.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.send_slow -> {
                    lightSpeed = 3.0
                }
                R.id.send_normal -> {
                    lightSpeed = 1.0
                }
                R.id.send_fast -> {
                    lightSpeed = 0.5
                }
            }
        }

        val reset_button: Button = findViewById(R.id.reset_button)
        reset_button.setOnClickListener() {
            val toast = Toast.makeText(this, "Przytrzymaj aby zresetować", Toast.LENGTH_SHORT)
            toast.show()
            countDownTimer = object : CountDownTimer(800,800) {
                override fun onTick(millisUntilFinished: Long) {
                }

                override fun onFinish() {
                    toast.cancel()
                }
            }.start()

        }

        reset_button.setOnLongClickListener() {
            lastLetter = 0
            lastSignal = 0
            last = 0
            lastTrainSignal = 0
            reps = 0
            tries = 0
            learnMode = true
            bestScore = "0 pkt"
            score = "0 pkt"
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
            true
        }
    }

    private fun shuffle(alphabet: String): String {
        val shuffled = alphabet.toCharArray().toMutableList()
        shuffled.shuffle()
        return shuffled.joinToString("")
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        finish()
    }
}