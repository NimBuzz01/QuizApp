package com.example.quizapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random


class QuizActivity : AppCompatActivity() {

    private lateinit var question1: TextView
    private lateinit var question2: TextView
    private lateinit var score: TextView
    private lateinit var timer: TextView
    private lateinit var correctTv: TextView
    private lateinit var greaterbtn: Button
    private lateinit var equalbtn: Button
    private lateinit var lessbtn: Button


    private var correct = 0
    private var total = 0

    private var expression1 = 0
    private var expression2 = 0

    private var secondsLeft = 50


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        question1 = findViewById(R.id.question1)
        question2 = findViewById(R.id.question2)
        score = findViewById(R.id.score)
        timer = findViewById(R.id.timer)
        correctTv = findViewById(R.id.correct)
        greaterbtn = findViewById(R.id.greaterbtn)
        equalbtn = findViewById(R.id.equalbtn)
        lessbtn = findViewById(R.id.lessbtn)

        nextQuestion()
        greaterbtn.setOnClickListener {
            nextQuestion()
            checkAnswer(1)
        }
        lessbtn.setOnClickListener {
            nextQuestion()
            checkAnswer(2)
        }
        equalbtn.setOnClickListener {
            nextQuestion()
            checkAnswer(3)
        }
        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                minusOneSecond()
                mainHandler.postDelayed(this, 1000)
            }
        })

    }

    fun minusOneSecond(){
        if (secondsLeft > 0) {
            secondsLeft -= 1
            timer.text = secondsLeft.toString()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun nextQuestion() {
        total += 1
        expression1 = 0
        expression2 = 0

        val scoreTv = "$correct/$total"
        score.text = scoreTv
        val term01Array = generateQuestion()
        val term01String = term01Array[0]
        expression1 = term01Array[1] as Int
        question1.text = "$term01String = $expression1"

        val term02Array = generateQuestion()
        val term02String = term02Array[0]
        expression2 = term02Array[1] as Int
        question2.text = "$term02String = $expression2"

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

    private fun generateQuestion(): Array<Any> {
        var expression = ""
        var expressionAnswer = 0
        val op1: String = getRandomOP()
        val op2: String = getRandomOP()
        val op3: String = getRandomOP()

        val num1: Int = Random.nextInt(1, 20)
        val num2: Int = Random.nextInt(1, 20)
        val num3: Int = Random.nextInt(1, 20)
        val num4: Int = Random.nextInt(1, 20)

        //Need to compare both expressions

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
    private fun getAnswer(n1: Int, n2: Int, op: String): Int {
        var answer = 0
        when (op) {
            "+" -> {
                answer = n1 + n2
            }
            "-" -> {
                answer = n1 - n2
            }
            "*" -> {
                answer = n1 * n2
            }
            "/" -> {
                answer = n1 / n2
            }
        }
        return answer
    }

    @SuppressLint("SetTextI18n")
    private fun checkAnswer(ans: Int) {
        var isTrue = false
        if (expression1 > expression2 && ans == 1) {
            isTrue = true
        } else
            if (expression1 < expression2 && ans == 2) {
                isTrue = true
            } else
                if (expression1 == expression2 && ans == 3) {
                    isTrue = true
                }

        if (isTrue) {
            correct += 1
            val scoreTv = "$correct/$total"
            score.text = scoreTv
            correctTv.text = "Correct!"
            correctTv.setTextColor(Color.parseColor("#09ff00"))
        } else {
            val scoreTv = "$correct/$total"
            score.text = scoreTv
            correctTv.text = "Wrong!"
            correctTv.setTextColor(Color.parseColor("#ff0000"))
        }
        if (correct % 5 == 0) {
            secondsLeft += 10
            Toast.makeText(applicationContext, "10 secs added!", Toast.LENGTH_SHORT).show()
        }

    }

}



