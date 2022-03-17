package com.example.quizapp

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

private lateinit var prefs: SharedPreferences

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = getSharedPreferences("saveData", MODE_PRIVATE)

        val newbtn = findViewById<Button>(R.id.newgamebtn)
        val aboutbtn = findViewById<Button>(R.id.aboutbtn)

        newbtn.setOnClickListener {
            prefs.edit().remove("total").apply()
            prefs.edit().remove("correct").apply()
            prefs.edit().remove("secondsLeft").apply()
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
        }

        aboutbtn.setOnClickListener(){
            showAlertDialog(it)
        }

    }

    private fun showAlertDialog(view: View) {
        MaterialAlertDialogBuilder(this)
            .setTitle("About Me")
            .setMessage("Student Name: Niamat Marjan\n" +
                    "Student ID: 20200492\n" +
                    "\n"+
                    "I confirm that I understand what plagiarism is and have read and " +
                    "understood the section on Assessment Offences in the Essential " +
                    "Information for Students. The work that I have submitted is " +
                    "entirely my own. Any work from other authors is duly referenced " +
                    "and acknowledged.\n")
            .setPositiveButton("Back"
            ) { dialog, which -> Toast.makeText(applicationContext,"...",Toast.LENGTH_LONG).show() }
            .show()
    }
}