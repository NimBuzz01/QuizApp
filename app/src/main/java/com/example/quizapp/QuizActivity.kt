package com.example.quizapp

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random


class QuizActivity : AppCompatActivity() {

    //Initialize all variables
    private lateinit var question1: TextView
    private lateinit var question2: TextView
    private lateinit var scoreQuiz: TextView
    private lateinit var timerQuiz: TextView
    private lateinit var correctQuiz: TextView
    private lateinit var greaterBtn: Button
    private lateinit var equalBtn: Button
    private lateinit var lessBtn: Button

    private lateinit var prefs: SharedPreferences
    val myHandler = Handler(Looper.getMainLooper())

    private var correct = 0
    private var total = 0
    private var secondsLeft = 50

    private var q1Value = 0
    private var q2Value = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        //Assign views to variables
        question1 = findViewById(R.id.question1)
        question2 = findViewById(R.id.question2)
        scoreQuiz = findViewById(R.id.score)
        timerQuiz = findViewById(R.id.timer)
        correctQuiz = findViewById(R.id.correct)
        greaterBtn = findViewById(R.id.greaterbtn)
        equalBtn = findViewById(R.id.equalbtn)
        lessBtn = findViewById(R.id.lessbtn)

        //Loading from Shared Preferences
        prefs = getSharedPreferences("saveData", MODE_PRIVATE)
        total = prefs.getInt("total", 0)
        correct = prefs.getInt("correct", correct)
        secondsLeft = prefs.getInt("secondsLeft", secondsLeft)


        nextQuestion()
        greaterBtn.setOnClickListener {
            checkAnswer(1)
            nextQuestion()
        }
        lessBtn.setOnClickListener {
            checkAnswer(2)
            nextQuestion()
        }
        equalBtn.setOnClickListener {
            checkAnswer(3)
            nextQuestion()
        }
        //Countdown Timer Handler
        myHandler.post(object : Runnable {
            override fun run() {
                countDownTimer()
                myHandler.postDelayed(this, 1000)
            }
        })
    }

    //Save Shared Preferences
    override fun onPause() {
        super.onPause()
        val editor = prefs.edit()
        editor.putInt("total", total)
        editor.putInt("correct", correct)
        editor.putInt("secondsLeft", secondsLeft)
        editor.apply()
    }

    //Countdown Timer
    fun countDownTimer() {
        if (secondsLeft > 0) {
            secondsLeft -= 1
            timerQuiz.text = secondsLeft.toString()
        } else {
            val intent = Intent(this, ScoreActivity::class.java)
            startActivity(intent)
            secondsLeft = 100000
            finish()
        }
    }

    private fun nextQuestion() {
        q1Value = 0
        q2Value = 0

        val scoreTv = "$correct/$total"
        scoreQuiz.text = scoreTv

        var random = Random.nextInt(1, 4)

        val q1Array = generateQuestion(random)
        val q1Text = q1Array[0]
        q1Value = q1Array[1] as Int

        val q2Array = generateQuestion(random)
        val q2Text = q2Array[0]
        q2Value = q2Array[1] as Int

        question1.text = "$q1Text = $q1Value"
        question2.text = "$q2Text = $q2Value"

    }

    private fun generateQuestion(noOfTerms: Int): Array<Any> {
        val num1 = (1..20).random()
        var questionValue = num1
        var questionText: String = num1.toString()
        repeat(noOfTerms) {
            var variableTerm = (1..20).random()
            when ((0..3).random()) {
                0 -> {                            // addition
                    if (questionValue + variableTerm >= 100) {
                        while (questionValue + variableTerm >= 100) {
                            variableTerm = (1..20).random()
                        }
                    }
                    questionText = if (questionText.length > 2) {
                        "($questionText)+$variableTerm"
                    } else {
                        "$questionText+$variableTerm"
                    }
                    questionValue += variableTerm
                }
                1 -> {                     //subtraction
                    if (questionValue - variableTerm >= 100) {
                        while (questionValue - variableTerm >= 100) {
                            variableTerm = (1..20).random()
                        }
                    }
                    questionText = if (questionText.length > 2) {
                        "($questionText)-$variableTerm"
                    } else {
                        "$questionText-$variableTerm"
                    }
                    questionValue -= variableTerm
                }
                2 -> {                     //multiplication
                    if (questionValue * variableTerm >= 100) {
                        while (questionValue * variableTerm >= 100) {
                            variableTerm = (1..20).random()
                        }
                    }
                    questionText = if (questionText.length > 2) {
                        "($questionText)*$variableTerm"
                    } else {
                        "$questionText*$variableTerm"
                    }
                    questionValue *= variableTerm
                }
                3 -> {                     //division
                    if (questionValue % variableTerm != 0) {
                        while (questionValue % variableTerm != 0) {
                            variableTerm = (1..20).random()
                        }
                    }
                    if (variableTerm == 0) {
                        while (variableTerm == 0) {
                            variableTerm = (1..20).random()
                        }
                    }
                    if (questionValue / variableTerm >= 100) {
                        while (questionValue / variableTerm >= 100) {
                            variableTerm = (1..20).random()
                        }
                    }
                    questionText = if (questionText.length > 2) {
                        "($questionText)/$variableTerm"
                    } else {
                        "$questionText/$variableTerm"
                    }
                    questionValue /= variableTerm
                }
            }
        }
        return arrayOf(questionText, questionValue)
    }

    private fun checkAnswer(ans: Int) {
        total += 1
        var isTrue = false
        if (q1Value > q2Value && ans == 1) {
            isTrue = true
        } else
            if (q1Value < q2Value && ans == 2) {
                isTrue = true
            } else
                if (q1Value == q2Value && ans == 3) {
                    isTrue = true
                }

        if (isTrue) {
            correct += 1
            val scoreTv = "$correct/$total"
            scoreQuiz.text = scoreTv
            correctQuiz.text = "Correct!"
            correctQuiz.setTextColor(Color.parseColor("#09ff00"))
        } else {
            val scoreTv = "$correct/$total"
            scoreQuiz.text = scoreTv
            correctQuiz.text = "Wrong!"
            correctQuiz.setTextColor(Color.parseColor("#ff0000"))
        }
        if (correct % 5 == 0 && isTrue) {
            secondsLeft += 10
            Toast.makeText(applicationContext, "10 secs added!", Toast.LENGTH_SHORT).show()
        }

    }

}



