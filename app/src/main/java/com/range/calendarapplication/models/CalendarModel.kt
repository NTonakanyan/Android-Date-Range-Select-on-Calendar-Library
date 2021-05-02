package com.range.calendarapplication.models

import com.range.calendarapplication.enums.CalendarType
import com.range.calendarapplication.enums.SelectedDayType
import com.range.calendarapplication.utils.DifItem
import java.util.*

data class CalendarModel(val type: CalendarType,
    val date: Calendar,
    val isSelectable: Boolean = true,
//    var isSelected: Boolean = false,
    var selectedDayType: SelectedDayType? = null) : DifItem<CalendarModel> {

    override fun areItemsTheSame(second: CalendarModel): Boolean {
        return this == second
    }

    override fun areContentsTheSame(second: CalendarModel): Boolean {
        return this == second
    }
}