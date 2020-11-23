package com.doubleb.meusemestre.ui.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.ui.viewholders.SemesterHistoryViewHolder

class SemesterHistoryAdapter(val list: List<List<Discipline>>) :
    RecyclerView.Adapter<SemesterHistoryViewHolder>() {

    private val pool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SemesterHistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.vh_semester_history, parent, false)
        )

    override fun onBindViewHolder(holder: SemesterHistoryViewHolder, position: Int) {
        holder.bind(list[position], pool)
    }

    override fun getItemCount() = list.size

}