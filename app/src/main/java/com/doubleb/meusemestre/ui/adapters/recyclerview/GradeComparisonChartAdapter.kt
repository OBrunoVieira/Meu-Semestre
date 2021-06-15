package com.doubleb.meusemestre.ui.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.GradeComparison
import com.doubleb.meusemestre.ui.adapters.recyclerview.diff.GradeComparisonDiffUtils
import com.doubleb.meusemestre.ui.viewholders.GradeComparisonChartViewHolder

class GradeComparisonChartAdapter :
    ListAdapter<GradeComparison, GradeComparisonChartViewHolder>(GradeComparisonDiffUtils()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GradeComparisonChartViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.vh_grade_comparison_chart, parent, false)
        )

    override fun onBindViewHolder(holder: GradeComparisonChartViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}