package com.doubleb.meusemestre.ui.views

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.models.GradeComparison
import com.doubleb.meusemestre.ui.adapters.recyclerview.GradeComparisonChartAdapter


class GradeComparisonChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : RecyclerView(context, attrs, defStyleAttr) {
    private val comparisonAdapter by lazy { GradeComparisonChartAdapter() }

    init {
        adapter = comparisonAdapter
        layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
    }

    fun submitList(gradeComparison: List<GradeComparison>) {
        comparisonAdapter.submitList(gradeComparison)
    }
}