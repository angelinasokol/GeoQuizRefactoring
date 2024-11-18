package com.example.geoquiz

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class CheatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = TextView(this)
        textView.textSize = 20f
        textView.setPadding(16, 16, 16, 16)
        textView.text = "CheatActivity"
        setContentView(textView)
    }
}