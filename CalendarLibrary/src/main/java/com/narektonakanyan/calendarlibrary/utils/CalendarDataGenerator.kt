package com.narektonakanyan.calendarlibrary.utils

import com.narektonakanyan.calendarlibrary.enums.CalendarType
import com.narektonakanyan.calendarlibrary.enums.SelectedDayType
import com.narektonakanyan.calendarlibrary.models.CalendarModel
import java.util.*

internal val list = mutableListOf<CalendarModel>()
internal val weekDaysOfName by lazy { getWeekDaysName().map { CalendarModel(CalendarType.DAY_OF_WEEK_NAME, it) } }

internal fun getGeneratedData(start: Calendar, end: Calendar): List<CalendarModel> {
    list.clear()
    var currentMillis: Long = start.timeInMillis
    val endMillis: Long = end.timeInMillis

    addMouthNameAndWeekDaysOfName(start)
    initListAfterSelectedDateToMouthStartDay(start)
    while (currentMillis <= endMillis) {
        val c = Calendar.getInstance().apply { timeInMillis = currentMillis }
        list.add(CalendarModel(CalendarType.DAY, c))
        currentMillis += 86400000L
        if (c.get(Calendar.DAY_OF_MONTH) == c.getActualMaximum(Calendar.DAY_OF_MONTH))
            addMouthNameAndWeekDaysOfName(Calendar.getInstance().apply { timeInMillis = currentMillis + 86400000L })
    }
    initListBeforeSelectedDateToMouthEndDay(end)
    return list
}

internal fun initListAfterSelectedDateToMouthStartDay(c: Calendar) {
    val count = c.get(Calendar.DAY_OF_MONTH)
    var currentMillis = c.timeInMillis.minus(count.times(86400000L))
    while (Calendar.getInstance().apply { timeInMillis = currentMillis }.get(Calendar.DAY_OF_WEEK) != 1) {
        currentMillis -= 86400000L
    }
    for (i in 0 until count) {
        list.add(CalendarModel(CalendarType.DAY, Calendar.getInstance().apply { timeInMillis = currentMillis }, false))
        currentMillis += 86400000L
    }
}

internal fun initListBeforeSelectedDateToMouthEndDay(c: Calendar) {
    val calendarDay: Int = c.get(Calendar.DAY_OF_MONTH)
    val mouthDayCount: Int = c.getActualMaximum(Calendar.DAY_OF_MONTH)
    val addedDayCountMillis: Long = (mouthDayCount - calendarDay).times(86400000L)
    val mouthLastDay = c.timeInMillis + addedDayCountMillis
    var currentMillis = c.timeInMillis
    while (currentMillis < mouthLastDay) {
        currentMillis += 86400000L
        list.add(CalendarModel(CalendarType.DAY, Calendar.getInstance().apply { timeInMillis = currentMillis }, false))
    }
}

internal fun addMouthNameAndWeekDaysOfName(c: Calendar) {
    list.add(CalendarModel(CalendarType.MOUTH, c))
    list.addAll(weekDaysOfName)
}

internal fun getWeekDaysName(): List<Calendar> {
    var currentMillis = 3 * 86400000L
    val list = mutableListOf<Calendar>()
    for (i in 0..6) {
        list.add(Calendar.getInstance().apply { timeInMillis = currentMillis })
        currentMillis += 86400000L
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

internal fun selectRange(selectedStart: Calendar, selectedEnd: Calendar): Int? {
    val firstSelectedIndex = list.indexOfFirst {
        it.date.get(Calendar.DAY_OF_YEAR) == selectedStart.get(Calendar.DAY_OF_YEAR) &&
                it.date.get(Calendar.YEAR) == selectedStart.get(Calendar.YEAR)
    }
    val lastSelectedIndex = list.indexOfLast {
        it.date.get(Calendar.DAY_OF_YEAR) == selectedEnd.get(Calendar.DAY_OF_YEAR) &&
                it.date.get(Calendar.YEAR) == selectedEnd.get(Calendar.YEAR)
    }
    if (firstSelectedIndex != -1 && lastSelectedIndex != -1)
        for (i in firstSelectedIndex..lastSelectedIndex) {
            if (list[i].isSelectable)
                when (i) {
                    firstSelectedIndex -> list[i].selectedDayType = SelectedDayType.START
                    lastSelectedIndex -> list[i].selectedDayType = SelectedDayType.END
                    else -> list[i].selectedDayType = SelectedDayType.MIDDLE
                }
        }
    return if (firstSelectedIndex == -1) null else firstSelectedIndex
}

internal fun selectSingle(calendar: Calendar): Int? {
    val index = list.indexOfFirst {
        it.date.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR) &&
                it.date.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
    }
    if (index != -1 && list[index].isSelectable)
        list[index].selectedDayType = SelectedDayType.SINGLE
    return if (index == -1) null else index
}

internal fun getSelectedDays(): List<Calendar> {
    return list.filter { it.selectedDayType != null }.map { it.date }
}