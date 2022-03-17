package com.example.quizapp

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ScoreActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    private var correct = 0
    private var total = 0
    private var incorrect = 0

    private lateinit var scoreF: TextView
    private lateinit var incorrectF: TextView



    private var secondsLeft = 50

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        scoreF = findViewById(R.id.scoreF)
        incorrectF = findViewById(R.id.incorrectF)

        prefs = getSharedPreferences("saveData", MODE_PRIVATE)
        total = prefs.getInt("total", 0)
        correct = prefs.getInt("correct", correct)
        incorrect = total - correct


        scoreF.text = "$correct out of $total"
        incorrectF.text = "$incorrect number of wrong"




    }
}