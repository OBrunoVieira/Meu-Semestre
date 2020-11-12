package com.doubleb.meusemestre.ui.views

import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

class TypeFaceSpannable(private val typeFace: Typeface?) : MetricAffectingSpan() {

    override fun updateMeasureState(textPaint: TextPaint) {
        applyTypeFace(textPaint, typeFace)
    }

    override fun updateDrawState(textPaint: TextPaint?) {
        textPaint?.let { applyTypeFace(textPaint, typeFace) }
    }

    private fun applyTypeFace(paint: Paint, typeFace: Typeface?) {
        paint.typeface = typeFace
    }

}