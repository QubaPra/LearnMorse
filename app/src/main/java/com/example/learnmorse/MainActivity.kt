package com.example.learnmorse

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

const val PREFS_NAME = "LearnMorsePrefs"

var lightSpeed = 1.0
var learn_alphabet = "ABCDEFGHIJKLMNOPRSTUWYZ"
var train_alphabet = "ABCDEFGHIJKLMNOPRSTUWYZ"
var alphabet = "ABCDEFGHIJKLMNOPRSTUWYZ"

class MainActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get SharedPreferences instance
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Load saved values for lightSpeedSetting, learn_alphabet and train_alphabet
        lightSpeed = sharedPreferences.getFloat("lightSpeed", 1.0F).toDouble()
        learn_alphabet = sharedPreferences.getString("learn_alphabet", "ABCDEFGHIJKLMNOPRSTUWYZ")!!
        train_alphabet = sharedPreferences.getString("train_alphabet", "ABCDEFGHIJKLMNOPRSTUWYZ")!!

        last = sharedPreferences.getInt("last", 0)
        reps = sharedPreferences.getInt("reps", 0)
        tries = sharedPreferences.getInt("tries", 0)
        learnMode = sharedPreferences.getBoolean("learnMode", true)
        isTrainTutorial = sharedPreferences.getBoolean("isTrainTutorial", true)

        lastLetter = sharedPreferences.getInt("lastLetter", 0)
        isLearnTutorial = sharedPreferences.getBoolean("isLearnTutorial", true)
        isMuted = sharedPreferences.getBoolean("isMuted", false)
        isTestTutorial = sharedPreferences.getBoolean("isTestTutorial", true)
        isSendTutorial = sharedPreferences.getBoolean("isSendTutorial", true)

        isSpeedTutorial = sharedPreferences.getBoolean("isSpeedTutorial", true)
        bestScore = sharedPreferences.getString("bestScore", "0 pkt")!!
        score = sharedPreferences.getString("score", "0 pkt")!!

        lastTrainSignal = sharedPreferences.getInt("lastTrainSignal", 0)


        val testButton: Button = findViewById(R.id.test)
        testButton.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
        }

        val naukaButton: Button = findViewById(R.id.nauka)
        naukaButton.setOnClickListener {
            val intent = Intent(this, LearnActivity::class.java)
            startActivity(intent)
        }

        val nadajButton: Button = findViewById(R.id.nadaj)
        nadajButton.setOnClickListener {
            val intent = Intent(this, SendAutoActivity::class.java)
            startActivity(intent)
        }

        val treningButton: Button = findViewById(R.id.trening)
        treningButton.setOnClickListener {
            val intent = Intent(this, TrainActivity::class.java)
            startActivity(intent)
        }

        val speedButton: Button = findViewById(R.id.speed)
        speedButton.setOnClickListener {
            val intent = Intent(this, SpeedStartActivity::class.java)
            startActivity(intent)
        }

        val zasadyButton: Button = findViewById(R.id.zasady)
        zasadyButton.setOnClickListener {
            val intent = Intent(this, RulesActivity::class.java)
            startActivity(intent)
        }
        zasadyButton.setOnLongClickListener {
            val toast = Toast.makeText(this, getString(R.string.authorInfo), Toast.LENGTH_SHORT)
            toast.show()
            true
        }

        val settingsButton: ImageView = findViewById(R.id.settings)
        settingsButton.setOnClickListener {
            val intent = Intent(this, MainSettingsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }
    }

}