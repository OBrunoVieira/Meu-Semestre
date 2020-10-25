package com.doubleb.meusemestre.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.ui.viewholders.DisciplineListViewHolder

class DisciplineListAdapter(var list: List<Discipline>? = null) :
    RecyclerView.Adapter<DisciplineListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DisciplineListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.vh_discipline_list, parent, false)
        )

    override fun getItemCount() = 1

    override fun onBindViewHolder(holder: DisciplineListViewHolder, position: Int) {
       list?.let { holder.bind(it) }
    }
}