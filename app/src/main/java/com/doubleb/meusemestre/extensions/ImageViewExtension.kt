package com.doubleb.meusemestre.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide

fun ImageView.tintColor(@ColorRes color: Int) = apply {
    setColorFilter(ContextCompat.getColor(context, color))
}

fun ImageView.loadRoundedImage(src : String?) = src.takeIfValid()?.let {
    Glide.with(this)
        .load(it)
        .circleCrop()
        .into(this)
}

fun ImageView.loadImage(src:String?) =  src.takeIfValid()?.let {
    Glide.with(this)
        .load(it)
        .into(this)
}

fun ImageView.showIfValidDrawable(drawable: Drawable?) =
    drawable?.let {
        this.visible()
        this.setImageDrawable(it)
    } ?: run {
        this.gone()
    }