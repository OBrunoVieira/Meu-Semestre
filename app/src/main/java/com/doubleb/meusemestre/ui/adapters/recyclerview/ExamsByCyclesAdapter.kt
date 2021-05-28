package com.doubleb.meusemestre.ui.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Exam
import com.doubleb.meusemestre.ui.viewholders.ExamsByCyclesViewHolder

class ExamsByCyclesAdapter(var list: List<Pair<Int?, List<Exam>>>? = null) :
    RecyclerView.Adapter<ExamsByCyclesViewHolder>() {

    private val pool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ExamsByCyclesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.vh_exams_by_cycles, parent, false)
        )

    override fun onBindViewHolder(holder: ExamsByCyclesViewHolder, position: Int) {
        list?.get(position)?.let { group ->
            val cycle = group.first ?: 0
            val examList = group.second
            holder.bind(cycle, examList, pool)
        }
    }

    override fun getItemCount() = list?.size ?: 0

}