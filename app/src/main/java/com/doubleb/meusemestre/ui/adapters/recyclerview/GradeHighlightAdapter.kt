package com.doubleb.meusemestre.ui.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.ui.viewholders.GradeHighlightViewHolder

class GradeHighlightAdapter(
    var bestDiscipline: Discipline? = null,
    var worstDiscipline: Discipline? = null,
    var gradeVariationByDisciplines : Map<String, Float?>? = null
) :
    RecyclerView.Adapter<GradeHighlightViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GradeHighlightViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.vh_grade_highlight, parent, false)
        )

    override fun onBindViewHolder(holder: GradeHighlightViewHolder, position: Int) {
        holder.bind(bestDiscipline, worstDiscipline, gradeVariationByDisciplines)
    }

    override fun getItemCount() = 1
}