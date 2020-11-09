package com.doubleb.meusemestre.extensions

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun View.disable() = apply{
    alpha = .5f
    isEnabled = false
}

fun View.enable() = apply{
    alpha = 1f
    isEnabled = true
}

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

fun View.blendColorAnimation(
    @ColorRes fromColor: Int,
    @ColorRes toColor: Int,
    propertyName: String
) {
    ObjectAnimator.ofObject(
        this, propertyName, ArgbEvaluator(),
        ContextCompat.getColor(context, fromColor),
        ContextCompat.getColor(context, toColor)
    ).apply {
        duration = 300
        start()
    }
}