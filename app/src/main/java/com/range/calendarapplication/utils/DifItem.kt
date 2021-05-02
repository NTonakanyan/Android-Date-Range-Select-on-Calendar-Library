package com.range.calendarapplication.utils

internal interface DifItem<T> {
	fun areItemsTheSame(second : T) : Boolean
	fun areContentsTheSame(second : T) : Boolean
}
