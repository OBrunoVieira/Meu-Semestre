package com.doubleb.meusemestre.ui.views

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.invisible
import com.doubleb.meusemestre.extensions.roundWhenBase10
import com.doubleb.meusemestre.extensions.takeIfValid
import com.doubleb.meusemestre.extensions.visible
import kotlinx.android.synthetic.main.view_average_indicator.view.*

class AverageIndicatorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val PROGRESS_DURATION = 600L
    }

    private val titleTextSize: Int
    private val titleMinLines: Int
    private val titleSpacing: Int

    private var previousProgress = 0

    init {
        View.inflate(context, R.layout.view_average_indicator, this)
        minWidth = resources.getDimensionPixelSize(R.dimen.max_discipline_width)

        context.obtainStyledAttributes(attrs, R.styleable.AverageIndicatorView, defStyleAttr, 0)
            .run {
                titleTextSize = getDimensionPixelSize(
                    R.styleable.AverageIndicatorView_aiv_title_height,
                    resources.getDimensionPixelSize(R.dimen.text_size_sixteen)
                )

                titleMinLines = getInteger(
                    R.styleable.AverageIndicatorView_aiv_title_min_lines,
                    2
                )

                titleSpacing = getDimensionPixelSize(
                    R.styleable.AverageIndicatorView_aiv_title_spacing,
                    resources.getDimensionPixelSize(R.dimen.spacing_eight)
                )

                recycle()
            }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        discipline_text_view_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize.toFloat())
        (discipline_text_view_title.layoutParams as MarginLayoutParams).bottomMargin = titleSpacing
        discipline_text_view_title.minLines = titleMinLines

        average(null)
    }

    fun title(title: String?) = apply {
        discipline_text_view_title.text = title
    }

    fun average(average: Float?) = apply {
        average?.takeIf { it >= 0 }?.let {
            average_indicator_text_view_average_title.text = transformGradeToText(average)

            val currentProgress = ((average / 10) * 100).toInt()
            runProgressAnimation(previousProgress, currentProgress)
            average_indicator_progress.visible()

            previousProgress = currentProgress
        } ?: run {
            average_indicator_text_view_average_title.setText(R.string.average_indicator_empty_average)
            average_indicator_progress.invisible()

            previousProgress = 0
        }
    }

    private fun runProgressAnimation(from: Int, to: Int) =
        ObjectAnimator.ofInt(average_indicator_progress, "progress", from, to)
            .apply {
                interpolator = AccelerateDecelerateInterpolator()
                duration = PROGRESS_DURATION
            }
            .start()

    fun recoverContentDescription(name: String?, average: Float?) =
        run {
            val discipline = name.takeIfValid() ?: context.getString(R.string.current_discipline)
            average?.takeIf { it >= 0 }?.let {
                context.getString(
                    R.string.average_content_description_valid,
                    discipline,
                    average.roundWhenBase10()
                )
            } ?: run {
                context.getString(R.string.average_content_description_invalid, discipline)
            }
        }

    private fun transformGradeToText(grade: Float) =
        HtmlCompat.fromHtml(
            context.getString(R.string.average_indicator_title, grade.roundWhenBase10()),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

}