package com.doubleb.meusemestre.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.drawableLeft
import com.doubleb.meusemestre.extensions.roundWhenBase10
import com.doubleb.meusemestre.extensions.tintBackgroundColorRes
import kotlinx.android.synthetic.main.view_grade_variation.view.*

class GradeVariationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_grade_variation, this)
    }

    fun name(name: String) = apply {
        grade_variation_text_view_title.text = name
    }

    fun grade(grade: Float) = apply {
        grade_variation_text_view_grade.text = grade.roundWhenBase10()
    }

    fun variation(variation: Float) = apply {
        if (variation < 0) {
            grade_variation_text_view_variation.tintBackgroundColorRes(R.color.light_pink)
            grade_variation_text_view_variation.drawableLeft(R.drawable.vector_variation_down)
        } else {
            grade_variation_text_view_variation.tintBackgroundColorRes(R.color.light_green)
            grade_variation_text_view_variation.drawableLeft(R.drawable.vector_variation_up)
        }

        grade_variation_text_view_variation.text = variation.roundWhenBase10()
    }
}