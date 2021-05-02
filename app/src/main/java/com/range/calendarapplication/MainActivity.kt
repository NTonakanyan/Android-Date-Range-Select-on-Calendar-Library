package com.range.calendarapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.range.calendarapplication.adapters.CalendarAdapter
import com.range.calendarapplication.databinding.ActivityMainBinding
import com.range.calendarapplication.enums.CalendarType
import com.range.calendarapplication.utils.getGeneratedData
import java.util.*


class MainActivity : AppCompatActivity() {

    private val _binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val _adapter by lazy { CalendarAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
        setupView()
    }

    private fun setupView() {
        _binding.apply {
            val start = Calendar.getInstance().apply { set(Calendar.YEAR, get(Calendar.YEAR) - 1) }.apply { timeInMillis += 20 * 86400000 }
            val end = Calendar.getInstance()

            val list = getGeneratedData(start, end)
            val gridLayoutManager = GridLayoutManager(this@MainActivity, 7)
            gridLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (list[position].type == CalendarType.MOUTH) {
                        return 7 //item will take 1/3 space of row
                    } else if (list[position].type == CalendarType.DAY_OF_WEEK_NAME) {
                        return 1 //you will have 2/3 space of row
                    } else if (list[position].type == CalendarType.DAY) {
                        return 1 //you will have full row size item
                    } else {
                        return 1
                    }
                }
            }

            recyclerView.adapter = _adapter
            recyclerView.layoutManager = gridLayoutManager

            _adapter.submitList(list)
        }
    }
}