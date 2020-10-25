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
    private var thereWasChanging = false

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

    fun grade(grade: Float) = apply {
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
        val title = context.getString(R.string.circle_chart_title)
        val result = grade.roundWhenBase10()
        val interLight = ResourcesCompat.getFont(context, R.font.inter_light)
        val interSemiBold = ResourcesCompat.getFont(context, R.font.inter_semi_bold)

        SpannableString("${title}\n${result}").apply {
            //============================ Span Title Text =========================================
            setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, R.color.zircon)),
                0,
                length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            setSpan(
                TypeFaceSpannable(interLight),
                0,
                title.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            //============================ Span Result Text =========================================
            setSpan(
                RelativeSizeSpan(1.8f),
                title.length,
                length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            setSpan(
                TypeFaceSpannable(interSemiBold),
                title.length,
                length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    private fun configurePieData(percentage: Float) =
        run {
            val entries = ArrayList<PieEntry>().apply {
                add(PieEntry(percentage))
                add(PieEntry(100 - percentage))
            }

            val colors = ArrayList<Int>().apply {
                add(ContextCompat.getColor(context, R.color.zircon))
                add(ContextCompat.getColor(context, R.color.zircon_forty))
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