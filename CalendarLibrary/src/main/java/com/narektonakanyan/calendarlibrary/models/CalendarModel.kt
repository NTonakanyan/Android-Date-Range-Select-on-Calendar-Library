package com.narektonakanyan.calendarlibrary.models

import com.narektonakanyan.calendarlibrary.enums.CalendarType
import com.narektonakanyan.calendarlibrary.enums.SelectedDayType
import com.narektonakanyan.calendarlibrary.utils.DifItem
import java.util.*

internal data class CalendarModel(val type: CalendarType,
    val date: Calendar,
    val isSelectable: Boolean = true,
    var selectedDayType: SelectedDayType? = null) : DifItem<CalendarModel> {

    override fun areItemsTheSame(second: CalendarModel): Boolean {
        return this == second
    }

    override fun areContentsTheSame(second: CalendarModel): Boolean {
        return this == second
    }
}