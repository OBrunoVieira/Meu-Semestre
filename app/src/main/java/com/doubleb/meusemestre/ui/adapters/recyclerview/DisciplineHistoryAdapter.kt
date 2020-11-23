package com.doubleb.meusemestre.ui.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.ui.adapters.recyclerview.diff.DisciplineDiffUtils
import com.doubleb.meusemestre.ui.viewholders.DisciplineHistoryViewHolder

class DisciplineHistoryAdapter :
    ListAdapter<Discipline, DisciplineHistoryViewHolder>(DisciplineDiffUtils()) {

    var parentPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DisciplineHistoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.vh_discipline_history, parent, false)
        )

    override fun onBindViewHolder(holder: DisciplineHistoryViewHolder, position: Int) {
        holder.bind(getItem(position), parentPosition)
    }
}