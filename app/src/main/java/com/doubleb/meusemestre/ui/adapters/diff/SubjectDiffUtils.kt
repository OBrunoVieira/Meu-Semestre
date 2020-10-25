package com.doubleb.meusemestre.ui.adapters.diff

import androidx.recyclerview.widget.DiffUtil
import com.doubleb.meusemestre.models.Subject

class SubjectDiffUtils : DiffUtil.ItemCallback<Subject>() {
    override fun areItemsTheSame(oldItem: Subject, newItem: Subject) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Subject, newItem: Subject) =
        oldItem == newItem
}