package com.doubleb.meusemestre.ui.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.ui.adapters.recyclerview.diff.DisciplineDiffUtils
import com.doubleb.meusemestre.ui.listeners.DisciplineListener
import com.doubleb.meusemestre.ui.viewholders.ActiveSemesterViewHolder

class ActiveSemesterAdapter(private val listener: DisciplineListener) :
    ListAdapter<Discipline, ActiveSemesterViewHolder>(DisciplineDiffUtils()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ActiveSemesterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.vh_active_semester, parent, false),
            listener
        )

    override fun onBindViewHolder(holder: ActiveSemesterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}