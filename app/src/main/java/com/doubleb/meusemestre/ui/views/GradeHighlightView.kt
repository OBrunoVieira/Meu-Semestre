package com.doubleb.meusemestre.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.gone
import com.doubleb.meusemestre.extensions.visible
import kotlinx.android.synthetic.main.view_grade_highlight.view.*

class GradeHighlightView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private var highestHighlightGradeName = ""
    private var lowestHighlightGradeName = ""
    private var highestHighlightGrade = 0f
    private var lowestHighlightGrade = 0f
    private var highestHighlightVariation = 0f
    private var lowestHighlightVariation = 0f

    init {
        View.inflate(context, R.layout.view_grade_highlight, this)
    }

    fun highestHighlight(name: String, grade: Float, variation: Float) = apply {
        this.highestHighlightGradeName = name
        this.highestHighlightGrade = grade
        this.highestHighlightVariation = variation
    }

    fun lowestHighlight(name: String, grade: Float, variation: Float) = apply {
        this.lowestHighlightGradeName = name
        this.lowestHighlightGrade = grade
        this.lowestHighlightVariation = variation
    }

    fun build() {
        if (highestHighlightVariation != 0f && lowestHighlightVariation != 0f) {
            grade_highlight_grade_vertical_positive.visible()
            grade_highlight_grade_vertical_negative.visible()
            grade_highlight_grade_horizontal.gone()

            grade_highlight_grade_vertical_positive
                .name(highestHighlightGradeName)
                .grade(highestHighlightGrade)
                .variation(highestHighlightVariation)

            grade_highlight_grade_vertical_negative
                .name(lowestHighlightGradeName)
                .grade(lowestHighlightGrade)
                .variation(lowestHighlightVariation)

        } else {
            grade_highlight_grade_horizontal.visible()
            grade_highlight_grade_vertical_positive.gone()
            grade_highlight_grade_vertical_negative.gone()

            val targetGradeName =
                if (highestHighlightVariation != 0f) highestHighlightGradeName else lowestHighlightGradeName
            val targetGrade =
                if (highestHighlightVariation != 0f) highestHighlightGrade else lowestHighlightGrade
            val targetVariation =
                if (highestHighlightVariation != 0f) highestHighlightVariation else lowestHighlightVariation

            grade_highlight_grade_horizontal
                .name(targetGradeName)
                .grade(targetGrade)
                .variation(targetVariation)
        }
    }
}