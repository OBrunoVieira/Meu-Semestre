package com.doubleb.meusemestre.ui.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.ui.adapters.recyclerview.diff.DisciplineDiffUtils
import com.doubleb.meusemestre.ui.listeners.DisciplineListener
import com.doubleb.meusemestre.ui.viewholders.DisciplineViewHolder

class DisciplineAdapter(var listener: DisciplineListener?=null) :
    ListAdapter<Discipline, DisciplineViewHolder>(DisciplineDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DisciplineViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.vh_discipline, parent, false),
            listener
        )

    override fun onBindViewHolder(holder: DisciplineViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
