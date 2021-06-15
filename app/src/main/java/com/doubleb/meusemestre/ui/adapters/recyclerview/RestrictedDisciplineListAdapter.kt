package com.doubleb.meusemestre.ui.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.models.Exam
import com.doubleb.meusemestre.ui.viewholders.RestrictedDisciplineListViewHolder

class RestrictedDisciplineListAdapter(
    var list: List<Discipline>? = null,
    var examsByDisciplines: Map<String, List<Exam>?>? = null,
) : RecyclerView.Adapter<RestrictedDisciplineListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RestrictedDisciplineListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.vh_restricted_discipline_list, parent, false)
        )

    override fun getItemCount() = 1

    override fun onBindViewHolder(holder: RestrictedDisciplineListViewHolder, position: Int) {
        list?.let { holder.bind(it, examsByDisciplines) }
    }
}