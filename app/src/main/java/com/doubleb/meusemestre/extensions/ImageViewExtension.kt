package com.doubleb.meusemestre.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun ImageView.tintColor(@ColorRes color: Int) = apply {
    setColorFilter(ContextCompat.getColor(context, color))
}

fun ImageView.showIfValidDrawable(drawable: Drawable?) =
    drawable?.let {
        this.visible()
        this.setImageDrawable(it)
    } ?: run {
        this.gone()
    }