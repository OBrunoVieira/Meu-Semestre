package com.doubleb.meusemestre.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.models.Discipline
import kotlinx.android.synthetic.main.vh_best_discipline.view.*

class BestDisciplineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(discipline: Discipline) {
        itemView.run {
            best_discipline_text_view_name.text = discipline.name
            best_discipline_circle_chart.grade(discipline.grade).build()
        }
    }

}