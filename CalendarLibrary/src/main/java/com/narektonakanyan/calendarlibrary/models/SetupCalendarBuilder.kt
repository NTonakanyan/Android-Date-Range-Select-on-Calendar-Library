package com.narektonakanyan.calendarlibrary.models

import java.util.*

class SetupCalendarBuilder(val start: Calendar,
    val end: Calendar,
    val selectedStart: Calendar?,
    val selectedEnd: Calendar?) {

    private constructor(builder: Builder) : this(
        builder.start,
        builder.end,
        builder.selectedStart,
        builder.selectedEnd
    )

    companion object {
        inline fun build(start: Calendar, end: Calendar, block: Builder.() -> Unit) = Builder(start, end).apply(block).build()
    }

    class Builder(val start: Calendar, val end: Calendar) {
        var selectedStart: Calendar? = null
        var selectedEnd: Calendar? = null

        fun selectedStart(selectedStart: Calendar?) = apply { this.selectedStart = selectedStart }

        fun selectedEnd(selectedEnd: Calendar?) = apply { this.selectedEnd = selectedEnd }

        fun build(): SetupCalendarBuilder {
            return SetupCalendarBuilder(this)
        }
    }
}