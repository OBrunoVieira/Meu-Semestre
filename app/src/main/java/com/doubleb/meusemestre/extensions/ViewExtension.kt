package com.doubleb.meusemestre.extensions

import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun View.tintBackgroundColorRes(@ColorRes color: Int) = apply {
    backgroundTintList = ContextCompat.getColorStateList(context, color)
}

fun View.tintBackgroundColor(@ColorInt color: Int) = apply {
    backgroundTintList = ColorStateList.valueOf(color)
}

fun View.visible() = apply {
    this.visibility = View.VISIBLE
}

fun View.gone() = apply {
    this.visibility = View.GONE
}

fun View.invisible() = apply {
    this.visibility = View.INVISIBLE
}