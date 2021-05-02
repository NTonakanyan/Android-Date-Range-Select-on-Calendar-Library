package com.range.calendarapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.range.calendarapplication.R
import com.range.calendarapplication.databinding.AdapterCalendarDayItemBinding
import com.range.calendarapplication.databinding.AdapterCalendarDayOfWeekNameItemBinding
import com.range.calendarapplication.databinding.AdapterCalendarMouthItemBinding
import com.range.calendarapplication.enums.CalendarType
import com.range.calendarapplication.enums.SelectedDayType
import com.range.calendarapplication.models.CalendarModel
import com.range.calendarapplication.utils.getDiffCallback
import com.range.calendarapplication.utils.selectItem
import java.text.SimpleDateFormat
import java.util.*

class CalendarAdapter : BaseMultiTypeListAdapter<CalendarModel, ViewBinding>(getDiffCallback()) {

    override fun inflate(inflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean): Map<Int, ViewBinding> {
        return mapOf(CalendarType.DAY.ordinal to AdapterCalendarDayItemBinding.inflate(inflater, parent, attachToParent),
            CalendarType.DAY_OF_WEEK_NAME.ordinal to AdapterCalendarDayOfWeekNameItemBinding.inflate(inflater, parent, attachToParent),
            CalendarType.MOUTH.ordinal to AdapterCalendarMouthItemBinding.inflate(inflater, parent, attachToParent))
    }

    override fun bindActions(): Map<Int, BaseViewHolder<ViewBinding, CalendarModel>.(data: CalendarModel) -> Unit> {
        return mapOf(
            CalendarType.DAY.ordinal to { item ->
                binding as AdapterCalendarDayItemBinding
                binding.apply {
                    day.text = item.date.get(Calendar.DAY_OF_MONTH).toString()
                    day.alpha = if (item.isSelectable) 1f else 0.3f
                    when (item.selectedDayType) {
                        SelectedDayType.START -> {
                            day.background = ContextCompat.getDrawable(holderContext, R.drawable.background_start_day)
                            day.setTextColor(ContextCompat.getColor(holderContext, R.color.orange))
                        }
                        SelectedDayType.END -> {
                            day.background = ContextCompat.getDrawable(holderContext, R.drawable.background_end_day)
                            day.setTextColor(ContextCompat.getColor(holderContext, R.color.orange))
                        }
                        SelectedDayType.MIDDLE -> {
                            day.setBackgroundColor(ContextCompat.getColor(holderContext, R.color.screamin_green_30))
                            day.setTextColor(ContextCompat.getColor(holderContext, R.color.white))
                        }
                        SelectedDayType.SINGLE -> {
                            day.background = ContextCompat.getDrawable(holderContext, R.drawable.background_day)
                            day.setTextColor(ContextCompat.getColor(holderContext, R.color.orange))
                        }
                        else -> {
                            day.setBackgroundColor(ContextCompat.getColor(holderContext, R.color.white))
                            day.setTextColor(ContextCompat.getColor(holderContext, R.color.black))
                        }
                    }
                    if (item.isSelectable)
                        day.setOnClickListener {
                            selectItem(item) { notifyStartPosition, notifyEndPosition ->
                                notifyItemRangeChanged(notifyStartPosition, notifyEndPosition - notifyStartPosition + 1)
                            }
                        }
                    else
                        day.setOnClickListener(null)
                }
            },
            CalendarType.DAY_OF_WEEK_NAME.ordinal to { item ->
                binding as AdapterCalendarDayOfWeekNameItemBinding
                binding.apply {
                    dayOfWeekName.text = convertDateToString(item.date.time, "EEE")
                }
            },
            CalendarType.MOUTH.ordinal to { item ->
                binding as AdapterCalendarMouthItemBinding
                binding.apply {
                    mouth.text = convertDateToString(item.date.time, "MMM yyyy")
                }
            },
        )
    }

    override fun isForType(item: CalendarModel): Int {
        return item.type.ordinal
    }

    private fun convertDateToString(date: Date, datePattern: String): String {
        val sdf = SimpleDateFormat(datePattern, Locale("en"))
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(date)
    }
}