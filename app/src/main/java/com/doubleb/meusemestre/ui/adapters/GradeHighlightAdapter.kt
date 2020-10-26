package com.doubleb.meusemestre.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.ui.viewholders.GradeHighlightViewHolder

class GradeHighlightAdapter : RecyclerView.Adapter<GradeHighlightViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GradeHighlightViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.vh_grade_highlight, parent, false)
        )

    override fun onBindViewHolder(holder: GradeHighlightViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = 1
}