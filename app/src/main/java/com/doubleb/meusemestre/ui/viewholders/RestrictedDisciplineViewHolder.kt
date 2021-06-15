package com.doubleb.meusemestre.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.gone
import com.doubleb.meusemestre.extensions.visible
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.models.Exam
import kotlinx.android.synthetic.main.vh_restricted_discipline.view.*

class RestrictedDisciplineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: Discipline, examsByDisciplines: Map<String, List<Exam>?>?) {
        itemView.run {
            restricted_discipline_text_view_title.text = item.name

            val gradeRegisteredList = item.id?.let { disciplineId ->
                examsByDisciplines?.getValue(disciplineId)?.filter { it.grade_result != null }
            }

            if (!gradeRegisteredList.isNullOrEmpty()) {
                restricted_discipline_text_view_info.visible()
                restricted_discipline_text_view_info.text =
                    context.getString(R.string.restricted_discipline_grades_registered, gradeRegisteredList.size)
            } else {
                restricted_discipline_text_view_info.gone()
            }

            restricted_discipline_circle_chart
                .colorIndex(bindingAdapterPosition)
                .backgroundProgressColorRes(R.color.light_gray)
                .valueProportion(1.2f)
                .holeRadius(80f)
                .enableTitle(false)
                .gradeResult(item.average)
                .build()
        }
    }
}