package com.example.learnmorse

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat


var last = 0
var lastTrainSignal = 0
var reps = 0
var tries = 0
var learnMode = true

var isTrainTutorial = true

class TrainActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    private lateinit var buttonDot: Button
    private lateinit var buttonMinus: Button
    private lateinit var textViewResult: TextView
    private lateinit var textViewRandom: TextView
    private lateinit var layout: RelativeLayout
    private lateinit var home: ImageView
    private lateinit var textView: TextView
    private lateinit var info: ImageView

    private lateinit var tutorialView: ConstraintLayout
    private lateinit var trainView: ConstraintLayout
    private lateinit var closeTutorial: ImageView

    private var mediaPlayer: MediaPlayer? = null
    private val mediaQueue: MutableList<MediaPlayer> = mutableListOf()

    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train)

        // Get SharedPreferences instance
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)


        val green = ContextCompat.getColor(this, R.color.accent1)
        val red = ContextCompat.getColor(this, R.color.accent2)
        val white = ContextCompat.getColor(this, R.color.white)
        buttonDot = findViewById(R.id.button_dot)
        buttonMinus = findViewById(R.id.button_minus)
        textViewResult = findViewById(R.id.textview_result)
        textViewRandom = findViewById(R.id.textview_random)
        layout = findViewById(R.id.content)
        home = findViewById(R.id.home)
        textView = findViewById(R.id.textView)
        tutorialView = findViewById(R.id.tutorial)
        trainView = findViewById(R.id.train)
        closeTutorial = findViewById(R.id.closeTutorial)

        val sound = findViewById<ImageView>(R.id.sound)
        info = findViewById(R.id.info)
        if (!isMuted) {
            sound.setImageResource(R.drawable.ic_unmute)
        } else {
            sound.setImageResource(R.drawable.ic_mute)
        }


        home.setOnClickListener {
            finish()
        }

        if (isTrainTutorial) {
            tutorialAnimation()
        }
        else {
            textViewRandom.text = train_alphabet[last].toString()
            if (learnMode) {
                textView.text = getString(R.string.trainTextViewLearn)
                textViewResult.text = getMorseCode(train_alphabet[last])

            } else {
                textView.text = getString(R.string.trainTextViewTest)
                textViewResult.text = ""
            }
        }

        closeTutorial.setOnClickListener {
            countDownTimer.cancel()
            trainView.visibility = View.VISIBLE
            tutorialView.visibility = View.GONE
            info.visibility = View.VISIBLE
            isTrainTutorial = false
            textViewRandom.text = train_alphabet[last].toString()
            if (learnMode) {
                textView.text = getString(R.string.trainTextViewLearn)
                textViewResult.text = getMorseCode(train_alphabet[last])
            } else {
                textView.text = getString(R.string.trainTextViewTest)
                textViewResult.text = ""
            }
        }


        info.setOnClickListener {
            if (!isTrainTutorial) {
                isTrainTutorial = true
                tutorialAnimation()
            }
        }




        sound.setOnClickListener {
            if (!isTrainTutorial) {
                if (isMuted) {
                    sound.setImageResource(R.drawable.ic_unmute)
                } else {
                    sound.setImageResource(R.drawable.ic_mute)
                }
                isMuted = !isMuted
            }
        }






        buttonDot.setOnClickListener {
            if (!isTrainTutorial) {
                if (learnMode) {
                    val text = textViewResult.text
                    if (lastTrainSignal < text.length) {
                        var color = 0
                        if (textViewResult.text[lastTrainSignal].toString() == "•") {
                            color = green
                        } else {
                            color = red
                        }

                        val coloredText = SpannableString(text.substring(0, lastTrainSignal + 1))
                        coloredText.setSpan(
                            ForegroundColorSpan(green),
                            0,
                            lastTrainSignal,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        coloredText.setSpan(
                            ForegroundColorSpan(color),
                            lastTrainSignal,
                            lastTrainSignal + 1,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        textViewResult.text =
                            TextUtils.concat(coloredText, text.substring(lastTrainSignal + 1))

                        lastTrainSignal++

                        if (color == red) {
                            buttonMinus.isClickable = false
                            buttonDot.isClickable = false

                            countDownTimer = object : CountDownTimer(1000, 1000) {
                                override fun onTick(millisUntilFinished: Long) {
                                }

                                override fun onFinish() {
                                    lastTrainSignal = 0
                                    val coloredText = SpannableString(text)
                                    coloredText.setSpan(
                                        ForegroundColorSpan(white),
                                        0,
                                        text.length,
                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                    )
                                    textViewResult.text = coloredText
                                    buttonMinus.isClickable = true
                                    buttonDot.isClickable = true
                                }
                            }.start()

                        } else {
                            if (!isMuted) {
                                val media = MediaPlayer.create(this, R.raw.dot)
                                mediaQueue.add(media)
                                playMediaQueue()
                            }
                            if (lastTrainSignal == text.length) {
                                buttonMinus.isClickable = false
                                buttonDot.isClickable = false

                                countDownTimer = object : CountDownTimer(1000, 1000) {
                                    override fun onTick(millisUntilFinished: Long) {
                                    }

                                    override fun onFinish() {
                                        reps++
                                        if (last >= train_alphabet.length - 1) {
                                            last += 1
                                            reps = 4
                                        }
                                        if (reps > 3) {
                                            learnMode = false
                                            textView.text = getString(R.string.trainTextViewTest)
                                            reps = 0
                                            last -= 3
                                            textViewRandom.text = train_alphabet[last].toString()
                                            textViewResult.text = ""
                                            lastTrainSignal = 0
                                            buttonMinus.isClickable = true
                                            buttonDot.isClickable = true
                                        } else {
                                            last++
                                            textViewRandom.text = train_alphabet[last].toString()
                                            textViewResult.text = getMorseCode(train_alphabet[last])
                                            lastTrainSignal = 0
                                            buttonMinus.isClickable = true
                                            buttonDot.isClickable = true
                                        }
                                    }
                                }.start()
                            }
                        }
                    }
                } else {
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

        }

        layout.setOnClickListener {
            if (!learnMode && !isTrainTutorial && textViewRandom.currentTextColor == getColor(R.color.white)) {
                checkResult()
            }

        }
        layout.setOnLongClickListener {
            if (!learnMode && !isTrainTutorial && textViewRandom.currentTextColor == getColor(R.color.white))
            {
                textViewResult.text = ""
            }
            true
        }

        buttonMinus.setOnClickListener {

            if (!isTrainTutorial) {
                if (learnMode) {
                    val text = textViewResult.text
                    if (lastTrainSignal < text.length) {
                        var color = 0
                        if (textViewResult.text[lastTrainSignal].toString() == "—") {
                            color = green

                        } else {
                            color = red

                        }

                        val coloredText = SpannableString(text.substring(0, lastTrainSignal + 1))
                        coloredText.setSpan(
                            ForegroundColorSpan(green),
                            0,
                            lastTrainSignal,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        coloredText.setSpan(
                            ForegroundColorSpan(color),
                            lastTrainSignal,
                            lastTrainSignal + 1,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        textViewResult.text =
                            TextUtils.concat(coloredText, text.substring(lastTrainSignal + 1))

                        lastTrainSignal++

                        if (color == red) {
                            buttonMinus.isClickable = false
                            buttonDot.isClickable = false

                            countDownTimer = object : CountDownTimer(1000, 1000) {
                                override fun onTick(millisUntilFinished: Long) {
                                }

                                override fun onFinish() {
                                    lastTrainSignal = 0
                                    val coloredText = SpannableString(text)
                                    coloredText.setSpan(
                                        ForegroundColorSpan(white),
                                        0,
                                        text.length,
                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                    )
                                    textViewResult.text = coloredText
                                    buttonMinus.isClickable = true
                                    buttonDot.isClickable = true
                                }
                            }.start()
                        } else {
                            if (!isMuted) {
                                val media = MediaPlayer.create(this, R.raw.minus)
                                mediaQueue.add(media)
                                playMediaQueue()
                            }
                            if (lastTrainSignal == text.length) {
                                buttonMinus.isClickable = false
                                buttonDot.isClickable = false

                                countDownTimer = object : CountDownTimer(1000, 1000) {
                                    override fun onTick(millisUntilFinished: Long) {
                                    }

                                    override fun onFinish() {
                                        reps++
                                        if (last >= train_alphabet.length - 1) {
                                            last += 1
                                            reps = 4
                                        } else if (reps > 3) {
                                            learnMode = false
                                            textView.text = getString(R.string.trainTextViewTest)
                                            reps = 0
                                            last -= 3
                                            textViewRandom.text = train_alphabet[last].toString()
                                            textViewResult.text = ""
                                            lastTrainSignal = 0
                                            buttonMinus.isClickable = true
                                            buttonDot.isClickable = true
                                        } else {
                                            last++

                                            textViewRandom.text = train_alphabet[last].toString()
                                            textViewResult.text = getMorseCode(train_alphabet[last])
                                            lastTrainSignal = 0
                                            buttonMinus.isClickable = true
                                            buttonDot.isClickable = true
                                        }
                                    }
                                }.start()
                            }


                        }

                    }
                } else {
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
        }
    }

    override fun onStop() {
        super.onStop()
        for (media in mediaQueue) {
            media.release()
        }
    }

    private fun checkResult() {

        val input = textViewResult.text.toString()
        if (input == getMorseCode(train_alphabet[last])) {
            textViewRandom.setTextColor(getColor(R.color.accent1))
            tries = 0
            if (last>= train_alphabet.length - 1) {
                reps = 4
            } else {
                last++
                reps++
            }
        } else {
            textViewRandom.setTextColor(getColor(R.color.accent2))
            tries++
            if (tries > 1) {
                last++
                tries = 0
                reps++
            }
        }
        buttonMinus.isClickable = false
        buttonDot.isClickable = false
        layout.isClickable = false
        if (last> train_alphabet.length - 1) {
            last = 0
            reps = 4
        }

        countDownTimer = object : CountDownTimer(1000,1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                if (reps > 3) {
                    learnMode = true
                    textView.text = getString(R.string.trainTextViewLearn)
                    reps = 0

                    textViewResult.text = getMorseCode(train_alphabet[last])
                    lastTrainSignal = 0
                } else {
                    textViewResult.text = ""
                }
                textViewRandom.text = train_alphabet[last].toString()
                textViewRandom.setTextColor(getColor(R.color.white))
                buttonMinus.isClickable = true
                buttonDot.isClickable = true
                layout.isClickable = true
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        lastTrainSignal = 0
        if (isTrainTutorial)
        {
            isTrainTutorial = false
        }
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
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
        lastTrainSignal = 0
        val tutorialTextView = findViewById<TextView>(R.id.tutorialTextView)
        val tutorialContent = findViewById<RelativeLayout>(R.id.tutorialContent)
        textView.text = getString(R.string.trainInfoTextView)
        tutorialView.visibility = View.VISIBLE
        trainView.visibility = View.GONE
        info.visibility = View.INVISIBLE

        countDownTimer = object : CountDownTimer(19000, 500) {
            override fun onTick(millisUntilFinished: Long) {
                when (((18000 - millisUntilFinished)*2)/1000/2.0) {
                    0.0 -> tutorialTextView.text=getString(R.string.trainTutorial1)
                    3.0 -> blinkAnimation(buttonDot)
                    3.5 -> blinkAnimation(buttonMinus)
                    4.0 -> blinkAnimation(buttonDot)
                    4.5 -> blinkAnimation(buttonMinus)
                    6.5 -> tutorialTextView.text=getString(R.string.trainTutorial2)
                    12.0 -> tutorialTextView.text=getString(R.string.trainTutorial3)
                    16.0 -> blinkAnimation(tutorialContent)
                    17.0 -> blinkAnimation(tutorialContent)
                }
            }

            override fun onFinish() {
                trainView.visibility = View.VISIBLE
                tutorialView.visibility = View.GONE
                info.visibility = View.VISIBLE
                isTrainTutorial = false

                textViewRandom.text = train_alphabet[last].toString()
                if (learnMode) {
                    textView.text = getString(R.string.trainTextViewLearn)
                    textViewResult.text = getMorseCode(train_alphabet[last])

                } else {
                    textView.text = getString(R.string.trainTextViewTest)
                    textViewResult.text = ""
                }
            }
        }.start()

    }
    override fun onPause() {
        super.onPause()
        val editor = sharedPreferences.edit()
        editor.putInt("last", last)
        editor.putInt("reps", reps)
        editor.putInt("tries", tries)
        editor.putBoolean("learnMode", learnMode)
        editor.putBoolean("isTrainTutorial", isTrainTutorial)
        editor.apply()
    }

}
