package com.doubleb.meusemestre.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.models.Discipline
import kotlinx.android.synthetic.main.vh_grade_highlight.view.*

class GradeHighlightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(
        bestDiscipline: Discipline?,
        worstDiscipline: Discipline?,
        gradeVariationByDisciplines: Map<String, Float?>?,
    ) {
        bestDiscipline?.let {
            itemView.vh_grade_highlight.highestHighlight(
                it.name,
                it.average,
                gradeVariationByDisciplines?.get(it.id)
            )
        }

        worstDiscipline?.let {
            itemView.vh_grade_highlight.lowestHighlight(
                it.name,
                it.average,
                gradeVariationByDisciplines?.get(it.id)
            )
        }

        itemView.vh_grade_highlight
            .build()
    }

}