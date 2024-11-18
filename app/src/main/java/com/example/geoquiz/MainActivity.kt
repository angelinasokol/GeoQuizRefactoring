package com.example.geoquiz

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.geoquiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()

    // Обработчик результата из CheatActivity
    private val cheatLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val answerShown = result.data?.getBooleanExtra("EXTRA_ANSWER_SHOWN", false) ?: false
                if (answerShown) {
                    Toast.makeText(this, "Вы использовали подсказку", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Настройка привязки интерфейса
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Обновление текущего вопроса
        updateQuestion()

        // Установка обработчиков для кнопок
        binding.btnTrue.setOnClickListener { checkAnswer(true) }
        binding.btnFalse.setOnClickListener { checkAnswer(false) }
        binding.btnNext.setOnClickListener { nextQuestion() }

        // Обработчик для кнопки CHEAT
        binding.btnCheat.setOnClickListener {
            val intent = Intent(this, CheatActivity::class.java)
            intent.putExtra("EXTRA_ANSWER_IS_TRUE", quizViewModel.checkAnswer(true))
            cheatLauncher.launch(intent)
        }

        // Восстановление состояния
        savedInstanceState?.let {
            quizViewModel.currentIndex = it.getInt("KEY_INDEX", 0)
            quizViewModel.correctAnswerCount = it.getInt("KEY_SCORE", 0)
            updateQuestion()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("KEY_INDEX", quizViewModel.currentIndex)
        outState.putInt("KEY_SCORE", quizViewModel.correctAnswerCount)
    }

    private fun updateQuestion() {
        binding.quiz.text = quizViewModel.currentQuestion
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val isCorrect = quizViewModel.checkAnswer(userAnswer)
        val message = if (isCorrect) "Правильно!" else "Неправильно!"
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        // Скрываем кнопки после ответа
        binding.btnTrue.visibility = View.INVISIBLE
        binding.btnFalse.visibility = View.INVISIBLE

        if (quizViewModel.isLastQuestion()) {
            binding.btnNext.visibility = View.INVISIBLE
            binding.btnNext.isEnabled = false
            showResult()
        }
    }

    private fun nextQuestion() {
        if (!quizViewModel.isLastQuestion()) {
            quizViewModel.moveToNextQuestion()
            updateQuestion()

            // Возвращаем кнопки после перехода к следующему вопросу
            binding.btnTrue.visibility = View.VISIBLE
            binding.btnFalse.visibility = View.VISIBLE
        }
    }

    private fun showResult() {
        Toast.makeText(
            this,
            "Ваш результат: ${quizViewModel.correctAnswerCount} из ${quizViewModel.totalQuestions}",
            Toast.LENGTH_LONG
        ).show()
    }

    class QuizViewModel : ViewModel() {

        private val questions = listOf(
            "Москва - столица России",
            "Берлин - столица Франции",
            "Париж - столица Франции",
            "Мадрид - столица Испании"
        )
        private val answers = listOf(true, false, true, true)

        var currentIndex = 0
        var correctAnswerCount = 0

        val currentQuestion: String
            get() = questions[currentIndex]

        val totalQuestions: Int
            get() = questions.size

        fun checkAnswer(userAnswer: Boolean): Boolean {
            val correctAnswer = answers[currentIndex]
            if (userAnswer == correctAnswer) {
                correctAnswerCount++
                return true
            }
            return false
        }

        fun moveToNextQuestion() {
            if (currentIndex < questions.size - 1) {
                currentIndex++
            }
        }

        fun isLastQuestion(): Boolean = currentIndex == questions.size - 1

        fun resetQuiz() {
            currentIndex = 0
            correctAnswerCount = 0
        }
    }
}
