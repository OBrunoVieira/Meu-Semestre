package com.doubleb.meusemestre.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.gone
import com.doubleb.meusemestre.extensions.roundWhenBase10
import com.doubleb.meusemestre.extensions.visible
import kotlinx.android.synthetic.main.view_average_indicator.view.*

class AverageIndicatorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_average_indicator, this)
        minWidth = resources.getDimensionPixelSize(R.dimen.max_discipline_width)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        average(null)
    }

    fun average(average: Float?) =
        average?.let {
            average_indicator_text_view_average_title.text = transformGradeToText(average)

            average_indicator_progress.progress = ((average / 10) * 100).toInt()
            average_indicator_progress.visible()
        } ?: run {
            average_indicator_text_view_average_title.setText(R.string.average_indicator_empty_average)
            average_indicator_progress.gone()
        }

    private fun transformGradeToText(grade: Float) =
        HtmlCompat.fromHtml(
            context.getString(R.string.average_indicator_title, grade.roundWhenBase10()),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

}