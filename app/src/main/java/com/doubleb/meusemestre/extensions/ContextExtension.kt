package com.doubleb.meusemestre.extensions

import android.content.Context
import androidx.core.content.ContextCompat
import com.doubleb.meusemestre.R

fun Context.swipeColorByPosition(position: Int) =
    ContextCompat.getColor(
        this,
        if (position % 2 == 0) R.color.light_purple
        else R.color.light_blue
    )

fun Context.swipeWaveByPosition(position: Int) =
    ContextCompat.getDrawable(
        this,
        if (position % 2 == 0) R.drawable.vector_wave_left
        else R.drawable.vector_wave_right
    )