package com.doubleb.meusemestre.ui.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Exam
import com.doubleb.meusemestre.ui.viewholders.CardGradeComparisonChartViewHolder

class CardGradeComparisonChartAdapter(var examsByDisciplines: Map<String, List<Exam>?> = emptyMap()) :
    RecyclerView.Adapter<CardGradeComparisonChartViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CardGradeComparisonChartViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.vh_card_grade_comparison_chart, parent, false)
        )

    override fun onBindViewHolder(holder: CardGradeComparisonChartViewHolder, position: Int) {
        holder.bind(examsByDisciplines)
    }

    override fun getItemCount() = 1
}