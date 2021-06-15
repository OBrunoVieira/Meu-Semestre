package com.doubleb.meusemestre.ui.viewholders

import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.models.GradeComparison
import com.doubleb.meusemestre.models.extensions.sumGradeResultByCycle
import kotlinx.android.synthetic.main.vh_grade_comparison_chart.view.*


class GradeComparisonChartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: GradeComparison) = itemView.run {
        val firstExamsCycle = item.exams.sumGradeResultByCycle(1)
        val secondExamsCycle = item.exams.sumGradeResultByCycle(2)
        val thirdExamsCycle = item.exams.sumGradeResultByCycle(3)

        updateScale(firstExamsCycle, grade_comparison_chart_image_first)
        updateScale(secondExamsCycle, grade_comparison_chart_image_second)
        updateScale(thirdExamsCycle, grade_comparison_chart_image_third)

        grade_comparison_chart_text.text = item.disciplineName
    }

    private fun updateScale(gradeResult: Float?, imageView: ImageView) =
        imageView.post {
            imageView.isVisible = gradeResult != null

            gradeResult?.let {
                val targetHeight = (itemView.grade_comparison_chart_content_view.height * it) / 10

                ValueAnimator.ofInt(0, targetHeight.toInt()).apply {
                    interpolator = AccelerateDecelerateInterpolator()
                    duration = 500
                    addUpdateListener { valueAnimator ->
                        imageView.layoutParams = imageView.layoutParams?.apply {
                            height = valueAnimator.animatedValue as Int
                        }
                    }
                }.start()
            }
        }
}