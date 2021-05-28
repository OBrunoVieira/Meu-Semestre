package com.doubleb.meusemestre.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Discipline
import kotlinx.android.synthetic.main.vh_restricted_discipline.view.*

class RestrictedDisciplineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: Discipline) {
        itemView.run {
            restricted_discipline_text_view_title.text = item.name
            restricted_discipline_circle_chart
                .colorIndex(bindingAdapterPosition)
                .backgroundProgressColorRes(R.color.light_gray)
                .valueProportion(1.2f)
                .holeRadius(80f)
                .enableTitle(false)
                .gradeResult(item.grade)
                .build()
        }
    }
}