package com.doubleb.meusemestre.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.doubleb.meusemestre.R
import kotlinx.android.synthetic.main.view_grade_highlight_horizontal.view.*

class GradeHighlightHorizontalView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_grade_highlight_horizontal, this)
        radius = resources.getDimension(R.dimen.spacing_eight)
        useCompatPadding = true
    }

    fun name(name: String) = apply {
        grade_highlight_horizontal_grade_variation.name(name)
    }

    fun grade(grade: Float) = apply {
        grade_highlight_horizontal_grade_variation.grade(grade)
    }

    fun variation(variation: Float) = apply {
        if (variation < 0) {
            setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_purple))
            grade_highlight_horizontal_image_view_wave.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.vector_wave_left
                )
            )
        } else {
            setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_blue))
            grade_highlight_horizontal_image_view_wave.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.vector_wave_right
                )
            )
        }

        grade_highlight_horizontal_grade_variation.variation(variation)
    }
}