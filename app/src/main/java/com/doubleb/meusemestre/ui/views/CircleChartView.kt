package com.doubleb.meusemestre.ui.views

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
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

    private val colors by lazy {
        arrayOf(
            R.color.light_purple,
            R.color.light_yellow,
            R.color.light_blue
        )
    }

    private val disableLightColor by lazy {
        ContextCompat.getColor(context, R.color.light_gray)
    }

    private val disableDarkColor by lazy {
        ContextCompat.getColor(context, R.color.dark_gray_forty)
    }

    private var gradeResult = 0f
    private var maxGrade = 10f
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
        gradeResult(0f).build()
    }

    fun colorIndex(index: Int) = apply {
        progressColorRes(colors[index % colors.size])
    }

    fun progressColorInt(@ColorInt color: Int) = apply {
        this.progressColor = color
    }

    fun progressColorRes(@ColorRes color: Int) = apply {
        this.progressColor = ContextCompat.getColor(context, color)
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

    fun valueProportion(proportion: Float) = apply {
        this.valueProportion = proportion
    }

    fun holeRadius(radius: Float) = apply {
        holeRadius = radius
    }

    fun gradeResult(@FloatRange(from = 0.0, to = 10.0) value: Float?) = apply {
        value?.let {
            thereWasChanging = gradeResult != it
            gradeResult = it
        }
    }

    fun maxGrade(@FloatRange(from = 0.0, to = 10.0) value: Float?) = apply {
        value?.let { maxGrade = it }
    }

    fun build() {
        if (thereWasChanging) {
            animateY(800, Easing.EaseInOutQuad)
            spin(1500, 0f, 270f, Easing.EaseInOutQuad)
        }

        centerText = configureCenteredText(gradeResult)
        data = PieData(configurePieData(percentage(gradeResult)))
    }

    private fun percentage(grade: Float) = (grade / maxGrade) * 100

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
            ForegroundColorSpan(if (gradeResult <= 0) disableDarkColor else progressColor),
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
                add(if (gradeResult <= 0) disableLightColor else progressColor)
                add(if (gradeResult <= 0) disableLightColor else backgroundProgressColor)
            }

            PieDataSet(entries, null).apply {
                setColors(colors)
                setDrawValues(false)
            }
        }
}