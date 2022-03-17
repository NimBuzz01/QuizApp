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
    private var secondsLeft = 10

    private var expression1 = 0
    private var expression2 = 0

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
        expression1 = 0
        expression2 = 0

        val scoreTv = "$correct/$total"
        scoreQuiz.text = scoreTv

        val term01Array = generateQuestion()
        val term01String = term01Array[0]
        expression1 = term01Array[1] as Int

        val term02Array = generateQuestion()
        val term02String = term02Array[0]
        expression2 = term02Array[1] as Int

        question1.text = "$term01String = $expression1"
        question2.text = "$term02String = $expression2"

    }

    private fun generateQuestion(): Array<Any> {
        var expression = ""
        var expressionAnswer = 0

        val op1: String = getRandomOP()
        val op2: String = getRandomOP()
        val op3: String = getRandomOP()

        val num1: Int = Random.nextInt(1, 21)
        val num2: Int = Random.nextInt(1, 21)
        val num3: Int = Random.nextInt(1, 21)
        val num4: Int = Random.nextInt(1, 21)


        when (Random.nextInt(1, 4)) {
            1 -> {
                expression = "$num1 $op1 $num2"
                expressionAnswer += getAnswer(num1, num2, op1)

            }
            2 -> {
                expression = "($num1 $op1 $num2) $op2 $num3"
                expressionAnswer += getAnswer(num1, num2, op1)
                expressionAnswer += getAnswer(expressionAnswer, num3, op2)

            }
            3 -> {
                expression = "(($num1 $op1 $num2) $op2 $num3) $op3 $num4"
                expressionAnswer += getAnswer(num1, num2, op1)
                expressionAnswer += getAnswer(expressionAnswer, num3, op2)
                expressionAnswer += getAnswer(expressionAnswer, num4, op3)
            }
        }


        return arrayOf(expression, expressionAnswer)
    }

    private fun getRandomOP(): String {
        val op: Int = Random.nextInt(0, 4)
        var operator = ""
        if (op == 0) {
            operator = "+"
        } else {
            if (op == 1) {
                operator = "-"
            } else {
                if (op == 2) {
                    operator = "*"
                } else {
                    if (op == 3) {
                        operator = "/"
                    }
                }
            }
        }
        return operator
    }

    private fun getAnswer(n1: Int, n2: Int, op: String): Int {
        var answer = 0
        var num1 = n1
        var num2 = n2
        when (op) {
            "+" -> {
                answer = num1 + num2
            }
            "-" -> {
                answer = num1 - num2
            }
            "*" -> {
                answer = num1 * num2
            }
            "/" -> {
                while (num1 % num2 != 0) {
                    num2 = Random.nextInt(1, 21)
                }
                answer = num1 / num2
            }
        }
        return answer
    }

    private fun checkAnswer(ans: Int) {
        total += 1
        var isTrue = false
        if (expression1 > expression2 && ans == 1) {
            isTrue = true
            println("greater $expression1  + $expression2")
        } else
            if (expression1 < expression2 && ans == 2) {
                isTrue = true
                println("lesser $expression1  + $expression2")
            } else
                if (expression1 == expression2 && ans == 3) {
                    isTrue = true
                    println("equal $expression1 + $expression2")
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



