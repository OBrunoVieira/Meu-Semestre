package com.doubleb.meusemestre.extensions

import android.widget.TextView
import androidx.annotation.DrawableRes

fun TextView.drawableLeft(@DrawableRes drawable: Int) =
    this.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)