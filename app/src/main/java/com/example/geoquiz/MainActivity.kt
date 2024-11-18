package com.example.geoquiz

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.geoquiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()

    private var isCheatUsed = false // Флаг для отслеживания использования подсказки
    private var cheatCount = 0 // Счетчик использованных подсказок
    private val maxCheats = 3 // Максимальное количество подсказок
    private lateinit var cheatCountTextView: TextView // Текстовое поле для отображения оставшихся подсказок

    // Обработчик результата из CheatActivity
    private val cheatLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val answerShown = result.data?.getBooleanExtra("EXTRA_ANSWER_SHOWN", false) ?: false
                if (answerShown) {
                    isCheatUsed = true
                    binding.btnCheat.visibility = View.INVISIBLE // Скрываем кнопку CHEAT
                    Toast.makeText(this, "Вы использовали подсказку", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cheatCountTextView = findViewById(R.id.cheatCountTextView)
        updateCheatCountDisplay()
        updateQuestion()

        binding.btnTrue.setOnClickListener { checkAnswer(true) }
        binding.btnFalse.setOnClickListener { checkAnswer(false) }
        binding.btnNext.setOnClickListener { nextQuestion() }

        // Обработчик для кнопки CHEAT
        binding.btnCheat.setOnClickListener {
            if (cheatCount < maxCheats) {
                val intent = Intent(this, CheatActivity::class.java)
                intent.putExtra("EXTRA_ANSWER_IS_TRUE", quizViewModel.getCorrectAnswer())
                cheatLauncher.launch(intent)
                cheatCount++ // Увеличиваем счетчик подсказок
                updateCheatCountDisplay() // Обновляем отображение подсказок
            } else {
                Toast.makeText(this, "Вы использовали все подсказки!", Toast.LENGTH_SHORT).show()
                binding.btnCheat.visibility = View.INVISIBLE // Скрываем кнопку, если подсказки закончились
            }
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

    private fun updateCheatCountDisplay() {
        val remainingCheats = maxCheats - cheatCount
        cheatCountTextView.text = "Осталось подсказок: $remainingCheats"
    }

    private fun updateQuestion() {
        binding.quiz.text = quizViewModel.currentQuestion
        isCheatUsed = false // Сбрасываем флаг при переходе к следующему вопросу
        binding.btnCheat.visibility = if (cheatCount < maxCheats) View.VISIBLE else View.INVISIBLE // Показываем кнопку, если есть доступные подсказки
        binding.btnTrue.visibility = View.VISIBLE // Делаем кнопки TRUE и FALSE видимыми
        binding.btnFalse.visibility = View.VISIBLE

        updateCheatCountDisplay() // Обновляем отображение подсказок
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val isCorrect = quizViewModel.checkAnswer(userAnswer)

        // Если была использована подсказка, не засчитываем правильный ответ
        val message = if (isCheatUsed) {
            "Вы использовали подсказку!"
        } else {
            if (isCorrect) {
                quizViewModel.correctAnswerCount++
                "Правильно!"
            } else {
                "Неправильно!"
            }
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        // Скрываем кнопки после ответа
        binding.btnTrue.visibility = View.INVISIBLE
        binding.btnFalse.visibility = View.INVISIBLE

        // Если это последний вопрос, скрываем кнопку Next и показываем результат
        if (quizViewModel.isLastQuestion()) {
            binding.btnNext.visibility = View.INVISIBLE
            binding.btnNext.isEnabled = false
            binding.btnCheat.visibility = View.INVISIBLE
            binding.btnCheat.isEnabled = false
            binding.quiz.visibility = View.INVISIBLE
            binding.quiz.isEnabled = false
            showResult()
        }
    }

    private fun nextQuestion() {
        if (!quizViewModel.isLastQuestion()) {
            quizViewModel.moveToNextQuestion()
            updateQuestion()
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
            return answers[currentIndex] == userAnswer
        }

        fun getCorrectAnswer(): Boolean {
            return answers[currentIndex]
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
