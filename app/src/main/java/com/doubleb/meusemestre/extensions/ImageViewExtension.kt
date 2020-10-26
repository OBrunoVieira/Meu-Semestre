package com.doubleb.meusemestre.extensions

import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun ImageView.tintColor(@ColorRes color: Int) = apply {
    setColorFilter(ContextCompat.getColor(context, color))
}