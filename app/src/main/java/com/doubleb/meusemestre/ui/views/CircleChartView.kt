package com.doubleb.meusemestre.ui.views

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ForegroundColorSpan
import android.text.style.MetricAffectingSpan
import android.text.style.RelativeSizeSpan
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.FloatRange
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.roundWhenBase10
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class CircleChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : PieChart(context, attrs, defStyleAttr) {

    private var grade = 0f
    private var valueProportion = 1.8f

    private var thereWasChanging = false
    private var enableTitle = true
    private var progressColor = ContextCompat.getColor(context, R.color.zircon)
    private var backgroundProgressColor = ContextCompat.getColor(context, R.color.zircon_forty)

    init {
        minimumWidth =
            minimumWidth.takeIf { it >= 0 } ?: resources.getDimension(R.dimen.chart_min_size)
                .toInt()
        minimumHeight =
            minimumHeight.takeIf { it >= 0 } ?: resources.getDimension(R.dimen.chart_min_size)
                .toInt()

        holeRadius = 90f
        transparentCircleRadius = 83f

        description.isEnabled = false
        legend.isEnabled = false

        isDragDecelerationEnabled = false
        setTouchEnabled(false)

        setDrawRoundedSlices(true)
        setHoleColor(Color.TRANSPARENT)

        setDrawCenterText(true)
        invalidate()

        loading()
    }

    fun loading() {
        grade(0f).build()
    }

    fun progressColorRes(@ColorRes color: Int) = apply {
        this.progressColor = ContextCompat.getColor(context, color)
    }

    fun progressColorInt(@ColorInt color: Int) = apply {
        this.progressColor = color
    }

    fun backgroundProgressColorRes(@ColorRes color: Int) = apply {
        this.backgroundProgressColor = ContextCompat.getColor(context, color)
    }

    fun backgroundProgressColorInt(@ColorInt color: Int) = apply {
        this.backgroundProgressColor = color
    }

    fun enableTitle(enableTitle: Boolean) = apply {
        this.enableTitle = enableTitle
    }

    fun valueProportion(proportion:Float) = apply {
        this.valueProportion = proportion
    }

    fun holeRadius(radius:Float) = apply {
        holeRadius = radius
    }

    fun grade(@FloatRange(from = 0.0, to = 10.0) grade: Float) = apply {
        this.thereWasChanging = this.grade != grade
        this.grade = grade
    }

    fun build() {
        if (thereWasChanging) {
            animateY(800, Easing.EaseInOutQuad)
            spin(1500, 0f, 270f, Easing.EaseInOutQuad)
        }

        centerText = configureCenteredText(grade)
        data = PieData(configurePieData(percentage(grade)))
    }

    private fun percentage(grade: Float) = (grade / 10) * 100

    private fun configureCenteredText(grade: Float) = run {
        val result = grade.roundWhenBase10()

        if (enableTitle) {
            val title = context.getString(R.string.circle_chart_title)
            SpannableString("${title}\n${result}").apply {
                buildTitleSpan(this, title.length)
                buildValueSpan(this, title.length)
            }
        } else {
            SpannableString(result).apply {
                buildTitleSpan(this)
                buildValueSpan(this)
            }
        }
    }

    private fun buildTitleSpan(spannableString: SpannableString, startLength: Int = 0) {
        val interLight = ResourcesCompat.getFont(context, R.font.inter_light)
        spannableString.setSpan(
            ForegroundColorSpan(progressColor),
            0,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableString.setSpan(
            TypeFaceSpannable(interLight),
            0,
            startLength,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private fun buildValueSpan(spannableString: SpannableString, startLength: Int = 0) {
        val interSemiBold = ResourcesCompat.getFont(context, R.font.inter_semi_bold)

        spannableString.setSpan(
            RelativeSizeSpan(valueProportion),
            startLength,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableString.setSpan(
            TypeFaceSpannable(interSemiBold),
            startLength,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private fun configurePieData(percentage: Float) =
        run {
            val entries = ArrayList<PieEntry>().apply {
                add(PieEntry(percentage))
                add(PieEntry(100 - percentage))
            }

            val colors = ArrayList<Int>().apply {
                add(progressColor)
                add(backgroundProgressColor)
            }

            PieDataSet(entries, null).apply {
                setColors(colors)
                setDrawValues(false)
            }
        }


    private inner class TypeFaceSpannable(private val typeFace: Typeface?) : MetricAffectingSpan() {

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
}