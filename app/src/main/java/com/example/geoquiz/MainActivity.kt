package com.example.geoquiz

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
    private var correctAnswerCount = 0

    companion object {
        private const val KEY_INDEX = "currentIndex"
        private const val KEY_SCORE = "correctAnswersCount"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        quiz = findViewById(R.id.quiz)
        btn_true = findViewById(R.id.btn_true)
        btn_false = findViewById(R.id.btn_false)
        btn_next = findViewById(R.id.btn_next)

        currentIndex = savedInstanceState?.getInt(KEY_INDEX) ?: 0
        correctAnswerCount = savedInstanceState?.getInt(KEY_SCORE) ?: 0
        updateQuestion()

        btn_true.setOnClickListener { checkAnswer(true) }
        btn_false.setOnClickListener { checkAnswer(false) }
        btn_next.setOnClickListener { nextQuestion() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_INDEX, currentIndex)
        outState.putInt(KEY_SCORE, correctAnswerCount)
    }

    private fun updateQuestion() {
        quiz.text = questions[currentIndex]
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = answers[currentIndex]
        if (userAnswer == correctAnswer) {
            correctAnswerCount++  // Увеличиваем счётчик правильных ответов
            Toast.makeText(this, "Правильно!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Неправильно!", Toast.LENGTH_SHORT).show()
        }

        btn_true.visibility = View.INVISIBLE
        btn_false.visibility = View.INVISIBLE

        if (currentIndex == questions.size - 1) {
            btn_next.visibility = View.INVISIBLE
            btn_next.isEnabled = false
            showResult()
        }
    }

    private fun nextQuestion() {
        if (currentIndex < questions.size - 1) {
            currentIndex++
            updateQuestion()

            btn_true.visibility = View.VISIBLE
            btn_false.visibility = View.VISIBLE
        }
    }

    private fun showResult() {
        Toast.makeText(
            this,
            "Ваш результат: $correctAnswerCount из ${questions.size}",
            Toast.LENGTH_LONG
        ).show()
    }
}
