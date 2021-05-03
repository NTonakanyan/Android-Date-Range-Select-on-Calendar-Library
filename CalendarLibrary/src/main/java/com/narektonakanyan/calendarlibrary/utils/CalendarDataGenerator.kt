package com.narektonakanyan.calendarlibrary.utils

import com.narektonakanyan.calendarlibrary.enums.CalendarType
import com.narektonakanyan.calendarlibrary.enums.SelectedDayType
import com.narektonakanyan.calendarlibrary.models.CalendarModel
import java.util.*

internal val list = mutableListOf<CalendarModel>()
internal val weekDaysOfName by lazy { getWeekDaysName().map { CalendarModel(CalendarType.DAY_OF_WEEK_NAME, it) } }

internal fun getGeneratedData(start: Calendar, end: Calendar): List<CalendarModel> {
    var currentMillis: Long = start.timeInMillis
    val endMillis: Long = end.timeInMillis

    addMouthNameAndWeekDaysOfName(start)
    initListAfterSelectedDateToMouthStartDay(start)
    while (currentMillis <= endMillis) {
        val c = Calendar.getInstance().apply { timeInMillis = currentMillis }
        list.add(CalendarModel(CalendarType.DAY, c))
        currentMillis += 86400000
        if (c.get(Calendar.DAY_OF_MONTH) == c.getActualMaximum(Calendar.DAY_OF_MONTH))
            addMouthNameAndWeekDaysOfName(Calendar.getInstance().apply { timeInMillis = currentMillis + 86400000 })
    }
    initListBeforeSelectedDateToMouthEndDay(end)
    return list
}

internal fun initListBeforeSelectedDateToMouthEndDay(c: Calendar) {
    val calendarDay: Int = c.get(Calendar.DAY_OF_MONTH)
    val mouthDayCount: Int = c.getActualMaximum(Calendar.DAY_OF_MONTH)
    val addedDayCountMillis: Long = (mouthDayCount - calendarDay).times(86400000L)
    val mouthLastDay = c.timeInMillis + addedDayCountMillis
    var currentMillis = c.timeInMillis
    while (currentMillis < mouthLastDay) {
        currentMillis += 86400000
        list.add(CalendarModel(CalendarType.DAY, Calendar.getInstance().apply { timeInMillis = currentMillis }, false))
    }
}

internal fun initListAfterSelectedDateToMouthStartDay(c: Calendar) {
    val count = c.get(Calendar.DAY_OF_MONTH)
    var currentMillis = c.timeInMillis.minus(count.times(86400000))
    while (Calendar.getInstance().apply { timeInMillis = currentMillis }.get(Calendar.DAY_OF_WEEK) != 1) {
        currentMillis -= 86400000
    }
    for (i in 0 until count) {
        list.add(CalendarModel(CalendarType.DAY, Calendar.getInstance().apply { timeInMillis = currentMillis }, false))
        currentMillis += 86400000
    }
}

internal fun addMouthNameAndWeekDaysOfName(c: Calendar) {
    list.add(CalendarModel(CalendarType.MOUTH, c))
    list.addAll(weekDaysOfName)
}

internal fun getWeekDaysName(): List<Calendar> {
    var currentMillis = 4 * 86400000L
    val list = mutableListOf<Calendar>()
    for (i in 0..6) {
        list.add(Calendar.getInstance().apply { timeInMillis = currentMillis })
        currentMillis += 86400000
    }
    return list
}

internal fun selectItem(model: CalendarModel, callback: (Int, Int) -> Unit) {

    fun selectSingle() {
        model.selectedDayType = SelectedDayType.SINGLE
        val index = list.indexOfFirst { it.selectedDayType != null }
        callback.invoke(index, index)
    }

    when (list.count { it.selectedDayType != null }) {
        0 -> {
            selectSingle()
        }
        1 -> {
            if (model == list.find { it.selectedDayType != null }) {
                val index = list.indexOfFirst { it.selectedDayType != null }
                model.selectedDayType = null
                callback.invoke(index, index)
                return
            }
            model.selectedDayType = SelectedDayType.END
            val firstSelectedIndex = list.indexOfFirst { it.selectedDayType != null }
            val lastSelectedIndex = list.indexOfLast { it.selectedDayType != null }
            for (i in firstSelectedIndex..lastSelectedIndex) {
                when (i) {
                    firstSelectedIndex -> list[i].selectedDayType = SelectedDayType.START
                    lastSelectedIndex -> list[i].selectedDayType = SelectedDayType.END
                    else -> list[i].selectedDayType = SelectedDayType.MIDDLE
                }
            }
            callback.invoke(firstSelectedIndex, lastSelectedIndex)
        }
        else -> {
            val firstSelectedIndex = list.indexOfFirst { it.selectedDayType != null }
            val lastSelectedIndex = list.indexOfLast { it.selectedDayType != null }
            for (i in firstSelectedIndex..lastSelectedIndex) {
                list[i].selectedDayType = null
            }
            callback.invoke(firstSelectedIndex, lastSelectedIndex)
            selectSingle()
        }
    }
}