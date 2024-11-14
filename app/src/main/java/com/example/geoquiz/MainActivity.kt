package com.example.geoquiz

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var quiz: TextView
    private lateinit var btn_true: Button
    private lateinit var btn_false: Button
    private lateinit var btn_next: Button

    private val questions = listOf(
        "Москва - столица России",
        "Берлин - столица Франции",
        "Париж - столица Франции",
        "Мадрид - столица Испании"
    )
    private val answers = listOf(true, false, true, true)
    private var currentIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        quiz = findViewById(R.id.quiz)
        btn_true = findViewById(R.id.btn_true)
        btn_false = findViewById(R.id.btn_false)
        btn_next = findViewById(R.id.btn_next)
    }
}