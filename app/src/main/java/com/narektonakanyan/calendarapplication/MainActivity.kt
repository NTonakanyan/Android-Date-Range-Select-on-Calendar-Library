package com.narektonakanyan.calendarapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.narektonakanyan.calendarapplication.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private val _binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
        setupView()
    }

    private fun setupView() {
        _binding.apply {
            val start = Calendar.getInstance().apply { set(Calendar.YEAR, get(Calendar.YEAR) - 1) }.apply { timeInMillis += 20 * 86400000 }
            val end = Calendar.getInstance()
            calendarView.setupData(start, end)
        }
    }
}