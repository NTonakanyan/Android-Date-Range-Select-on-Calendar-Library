package com.narektonakanyan.calendarapplication

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.narektonakanyan.calendarapplication.databinding.ActivityMainBinding
import com.narektonakanyan.calendarlibrary.models.ColorModel
import com.narektonakanyan.calendarlibrary.models.SetupCalendarBuilder
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
            val start = Calendar.getInstance().apply { set(Calendar.YEAR, get(Calendar.YEAR) - 1) }.apply { timeInMillis += 20 * 86400000L }
            val end = Calendar.getInstance()
            calendarView.setupData(SetupCalendarBuilder.Builder(start, end)
                .selectedStart(Calendar.getInstance().apply { set(Calendar.YEAR, get(Calendar.YEAR)) }.apply { timeInMillis -= 20 * 86400000L })
                .selectedEnd(Calendar.getInstance().apply { set(Calendar.YEAR, get(Calendar.YEAR)) }.apply { timeInMillis -= 5 * 86400000L })
                .setColorModel(ColorModel())
                .build())
        }
    }

    fun onClick(view: View) {
        val a = _binding.calendarView.getSelectedDate()
        print(a)
    }
}