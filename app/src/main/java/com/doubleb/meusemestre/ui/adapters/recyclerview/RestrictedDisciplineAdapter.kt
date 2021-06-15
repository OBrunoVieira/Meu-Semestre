package com.doubleb.meusemestre.ui.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.models.Exam
import com.doubleb.meusemestre.ui.adapters.recyclerview.diff.DisciplineDiffUtils
import com.doubleb.meusemestre.ui.viewholders.RestrictedDisciplineViewHolder

class RestrictedDisciplineAdapter (var examsByDisciplines: Map<String, List<Exam>?>? = null):
    ListAdapter<Discipline, RestrictedDisciplineViewHolder>(DisciplineDiffUtils()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RestrictedDisciplineViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.vh_restricted_discipline, parent, false)
        )

    override fun onBindViewHolder(holder: RestrictedDisciplineViewHolder, position: Int) {
        holder.bind(getItem(position), examsByDisciplines)
    }
}