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
import com.narektonakanyan.calendarlibrary.models.ColorModel
import com.narektonakanyan.calendarlibrary.models.SetupCalendarBuilder
import com.narektonakanyan.calendarlibrary.utils.getGeneratedData
import com.narektonakanyan.calendarlibrary.utils.getSelectedDays
import com.narektonakanyan.calendarlibrary.utils.selectRange
import com.narektonakanyan.calendarlibrary.utils.selectSingle

class CalendarView : FrameLayout {

    private val _binding by lazy { LayoutCalendarBinding.inflate(LayoutInflater.from(context), this, true) }
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
            recyclerView.layoutManager = gridLayoutManager
        }
    }

    private fun submitList(list: List<CalendarModel>, colorModel: ColorModel?) {
        val adapter = CalendarAdapter(colorModel ?: ColorModel())
        _binding.recyclerView.adapter = adapter
        _list.clear()
        _list.addAll(list)
        adapter.submitList(list)
    }

    fun setupData(builder: SetupCalendarBuilder) {
        val list = getGeneratedData(builder.start, builder.end)
        val index = when {
            builder.selectedStart != null && builder.selectedEnd != null -> selectRange(builder.selectedStart, builder.selectedEnd)
            builder.selectedStart != null -> selectSingle(builder.selectedStart)
            builder.selectedEnd != null -> selectSingle(builder.selectedEnd)
            else -> null
        }
        submitList(list, builder.colorModel)
        _binding.recyclerView.layoutManager?.scrollToPosition(index ?: list.size.minus(1))
    }

    fun getSelectedDate() = getSelectedDays()
}