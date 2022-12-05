package com.narektonakanyan.calendarlibrary.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.narektonakanyan.calendarlibrary.R
import com.narektonakanyan.calendarlibrary.databinding.AdapterCalendarDayItemBinding
import com.narektonakanyan.calendarlibrary.databinding.AdapterCalendarDayOfWeekNameItemBinding
import com.narektonakanyan.calendarlibrary.databinding.AdapterCalendarMouthItemBinding
import com.narektonakanyan.calendarlibrary.enums.CalendarType
import com.narektonakanyan.calendarlibrary.enums.SelectedDayType
import com.narektonakanyan.calendarlibrary.models.CalendarModel
import com.narektonakanyan.calendarlibrary.models.ColorModel
import com.narektonakanyan.calendarlibrary.utils.getDiffCallback
import com.narektonakanyan.calendarlibrary.utils.selectItem
import java.text.SimpleDateFormat
import java.util.*

internal class CalendarAdapter(private val _colorModel:ColorModel) : BaseMultiTypeListAdapter<CalendarModel, ViewBinding>(getDiffCallback()) {

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
                            background.background = ContextCompat.getDrawable(holderContext, android.R.color.transparent)
                            day.background = ContextCompat.getDrawable(holderContext, _colorModel.selectedStartTextBackground)
                            day.setTextColor(ContextCompat.getColor(holderContext, _colorModel.selectedTextColor))
                        }
                        SelectedDayType.END -> {
                            background.background = ContextCompat.getDrawable(holderContext, android.R.color.transparent)
                            day.background = ContextCompat.getDrawable(holderContext,_colorModel.selectedEndTextBackground)
                            day.setTextColor(ContextCompat.getColor(holderContext, _colorModel.selectedTextColor))
                        }
                        SelectedDayType.MIDDLE -> {
                            background.background = ContextCompat.getDrawable(holderContext, android.R.color.transparent)
                            day.setBackgroundColor(ContextCompat.getColor(holderContext, _colorModel.selectedMiddleTextBackground))
                            day.setTextColor(ContextCompat.getColor(holderContext, _colorModel.selectedMiddleTextColor))
                        }
                        SelectedDayType.SINGLE -> {
                            background.background = ContextCompat.getDrawable(holderContext, _colorModel.singleTextBackground)
                            day.setBackgroundColor(ContextCompat.getColor(holderContext, android.R.color.transparent))
                            day.setTextColor(ContextCompat.getColor(holderContext, R.color.orange))
                        }
                        else -> {
                            background.background = ContextCompat.getDrawable(holderContext, android.R.color.transparent)
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
        val sdf = SimpleDateFormat(datePattern, Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(date)
    }
}