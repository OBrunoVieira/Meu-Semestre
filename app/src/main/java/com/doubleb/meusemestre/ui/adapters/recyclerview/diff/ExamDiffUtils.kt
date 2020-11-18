package com.doubleb.meusemestre.ui.adapters.recyclerview.diff

import androidx.recyclerview.widget.DiffUtil
import com.doubleb.meusemestre.models.Exam

class ExamDiffUtils : DiffUtil.ItemCallback<Exam>() {
    override fun areItemsTheSame(oldItem: Exam, newItem: Exam) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Exam, newItem: Exam) =
        oldItem == newItem
}