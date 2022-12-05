package com.narektonakanyan.calendarlibrary.models

import com.narektonakanyan.calendarlibrary.R

data class ColorModel(
    val selectedTextColor: Int = R.color.orange,
    val selectedMiddleTextColor: Int = R.color.white,
    val selectedStartTextBackground: Int = R.drawable.background_start_day,
    val selectedMiddleTextBackground: Int = R.color.screamin_green_30,
    val selectedEndTextBackground: Int = R.drawable.background_end_day,
    val singleTextBackground: Int = R.drawable.background_day,
)