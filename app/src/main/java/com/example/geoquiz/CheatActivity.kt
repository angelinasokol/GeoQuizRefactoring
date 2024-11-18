package com.example.geoquiz

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.geoquiz.databinding.ActivityCheatBinding

class CheatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val answerIsTrue = intent.getBooleanExtra("EXTRA_ANSWER_IS_TRUE", false)
        binding.btnShowAnswer.setOnClickListener {
            binding.txtAnswer.text = if (answerIsTrue) "Правда" else "Ложь"
            setAnswerShownResult(true)
        }

        binding.apiLevelTextView.text = getString(R.string.api_level_text, Build.VERSION.SDK_INT)
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        setResult(RESULT_OK, intent.putExtra("EXTRA_ANSWER_SHOWN", isAnswerShown))
    }
}
