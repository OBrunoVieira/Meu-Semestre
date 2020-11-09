package com.doubleb.meusemestre.ui.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.ui.viewholders.BestDisciplineViewHolder

class BestDisciplineAdapter(var discipline: Discipline? = null) :
    RecyclerView.Adapter<BestDisciplineViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BestDisciplineViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.vh_best_discipline, parent, false)
        )

    override fun onBindViewHolder(holder: BestDisciplineViewHolder, position: Int) {
        discipline?.let { holder.bind(it) }
    }

    override fun getItemCount() = 1
}