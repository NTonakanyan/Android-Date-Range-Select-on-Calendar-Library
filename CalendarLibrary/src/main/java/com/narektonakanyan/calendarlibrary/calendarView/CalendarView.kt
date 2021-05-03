package com.narektonakanyan.calendarlibrary.calendarView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.narektonakanyan.calendarlibrary.adapters.CalendarAdapter
import com.narektonakanyan.calendarlibrary.databinding.LayoutCalendarBinding
import com.narektonakanyan.calendarlibrary.enums.CalendarType
import com.narektonakanyan.calendarlibrary.models.CalendarModel
import com.narektonakanyan.calendarlibrary.utils.getGeneratedData
import java.util.*

class CalendarView : FrameLayout {

    private val _binding by lazy { LayoutCalendarBinding.inflate(LayoutInflater.from(context), this, true) }
    private val _adapter by lazy { CalendarAdapter() }
    private val _list = mutableListOf<CalendarModel>()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {
        init()
    }

    private fun init() {
        _binding.apply {
            val gridLayoutManager = GridLayoutManager(context, 7)
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (_list[position].type) {
                        CalendarType.MOUTH -> 7
                        CalendarType.DAY_OF_WEEK_NAME -> 1
                        CalendarType.DAY -> 1
                    }
                }
            }

            recyclerView.adapter = _adapter
            recyclerView.layoutManager = gridLayoutManager
        }
    }

    private fun submitList(list: List<CalendarModel>) {
        _list.clear()
        _list.addAll(list)
        _adapter.submitList(list)
    }

    fun setupData(start:Calendar,end:Calendar){
        val list = getGeneratedData(start, end)
        submitList(list)
    }
}