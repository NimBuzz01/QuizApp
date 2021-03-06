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
    private lateinit var correctF: TextView
    private lateinit var incorrectF: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        scoreF = findViewById(R.id.scoreF)
        correctF = findViewById(R.id.correctF)
        incorrectF = findViewById(R.id.incorrectF)

        //Setting Shared Preferences
        prefs = getSharedPreferences("saveData", MODE_PRIVATE)
        total = prefs.getInt("total", 0)
        correct = prefs.getInt("correct", correct)
        incorrect = total - correct

        //Assigning to TextViews
        scoreF.text = "$correct/$total"
        correctF.text = "$correct"
        incorrectF.text = "$incorrect"




    }
}