package com.doubleb.meusemestre.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.vh_grade_highlight.view.*

class GradeHighlightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind() {
        itemView.vh_grade_highlight
            .highestHighlight("Cálculo I", 8.2f, 4f)
            .lowestHighlight("Desenvolvimento de Banco de Dados II", 9.0f, -0.2f)
            .build()
    }

}