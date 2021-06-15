package com.doubleb.meusemestre.ui.adapters.recyclerview.diff

import androidx.recyclerview.widget.DiffUtil
import com.doubleb.meusemestre.models.GradeComparison

class GradeComparisonDiffUtils : DiffUtil.ItemCallback<GradeComparison>() {
    override fun areItemsTheSame(oldItem: GradeComparison, newItem: GradeComparison) =
        oldItem.disciplineName == newItem.disciplineName

    override fun areContentsTheSame(oldItem: GradeComparison, newItem: GradeComparison) =
        oldItem == newItem
}