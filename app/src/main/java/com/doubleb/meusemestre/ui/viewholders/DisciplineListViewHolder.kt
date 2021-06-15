package com.doubleb.meusemestre.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.models.Exam
import com.doubleb.meusemestre.ui.adapters.recyclerview.DisciplineAdapter
import com.doubleb.meusemestre.ui.listeners.DisciplineListener
import kotlinx.android.synthetic.main.vh_discipline_list.view.*

class DisciplineListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val disciplineAdapter by lazy { DisciplineAdapter() }

    init {
        itemView.discipline_list_recycler_view.adapter = disciplineAdapter
    }

    fun bind(list: List<Discipline>, examsByDisciplines: Map<String, List<Exam>?>?, listener: DisciplineListener?) {
        disciplineAdapter.listener = listener
        disciplineAdapter.examsByDisciplines = examsByDisciplines
        disciplineAdapter.submitList(list)
    }

}