package com.doubleb.meusemestre.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.ui.adapters.recyclerview.RestrictedDisciplineAdapter
import kotlinx.android.synthetic.main.vh_restricted_discipline_list.view.*

class RestrictedDisciplineListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val restrictedDisciplineAdapter by lazy { RestrictedDisciplineAdapter() }

    init {
        itemView.restricted_discipline_list_recycler_view.adapter = restrictedDisciplineAdapter
    }

    fun bind(list: List<Discipline>) {
        restrictedDisciplineAdapter.submitList(list)
    }

}