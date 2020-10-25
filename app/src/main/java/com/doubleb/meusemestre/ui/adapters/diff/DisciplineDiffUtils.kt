package com.doubleb.meusemestre.ui.adapters.diff

import androidx.recyclerview.widget.DiffUtil
import com.doubleb.meusemestre.models.Discipline

class DisciplineDiffUtils : DiffUtil.ItemCallback<Discipline>() {
    override fun areItemsTheSame(oldItem: Discipline, newItem: Discipline) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Discipline, newItem: Discipline) =
        oldItem == newItem
}