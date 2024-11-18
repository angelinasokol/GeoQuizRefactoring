package com.example.geoquiz

import android.content.Intent
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
            val answerText = if (answerIsTrue) "Правда" else "Ложь"
            binding.txtAnswer.text = answerText
            val resultIntent = Intent()
            resultIntent.putExtra("EXTRA_ANSWER_SHOWN", true)
            setResult(RESULT_OK, resultIntent)
        }
    }
}
