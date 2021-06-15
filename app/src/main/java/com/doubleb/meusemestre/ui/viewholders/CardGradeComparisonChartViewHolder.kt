package com.doubleb.meusemestre.ui.viewholders

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.models.Exam
import com.doubleb.meusemestre.models.GradeComparison
import kotlinx.android.synthetic.main.vh_card_grade_comparison_chart.view.*


class CardGradeComparisonChartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(examsByDisciplines: Map<String, List<Exam>?>) = itemView.run {

        val gradeComparison = examsByDisciplines.map { GradeComparison(it.key, it.value) }
        val hasFirstCycle = gradeComparison.any { it.exams?.any { exam -> exam.cycle == 1 } == true }
        val hasSecondCycle = gradeComparison.any { it.exams?.any { exam -> exam.cycle == 2 } == true }
        val hasThirdCycle = gradeComparison.any { it.exams?.any { exam -> exam.cycle == 3 } == true }


        card_grade_comparison_chart_view_legend_first.isVisible = hasFirstCycle
        card_grade_comparison_chart_view_legend_second.isVisible = hasSecondCycle
        card_grade_comparison_chart_view_legend_third.isVisible = hasThirdCycle

        itemView.card_grade_comparison_chart_view.submitList(gradeComparison)
    }

}