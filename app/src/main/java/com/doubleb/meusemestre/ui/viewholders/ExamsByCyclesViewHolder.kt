package com.doubleb.meusemestre.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Exam
import com.doubleb.meusemestre.ui.adapters.recyclerview.ExamsAdapter
import kotlinx.android.synthetic.main.vh_exams_by_cycles.view.*

class ExamsByCyclesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val adapter by lazy { ExamsAdapter() }

    init {
        itemView.exam_by_cycles_recycler_view.adapter = adapter
    }

    fun bind(cycle: Int, list: List<Exam>, pool: RecyclerView.RecycledViewPool) {
        itemView.run {
            exam_by_cycles_text_view_title.text =
                context.getString(R.string.exams_by_cycles_title, cycle)

            val layoutManager = exam_by_cycles_recycler_view.layoutManager as LinearLayoutManager
            layoutManager.initialPrefetchItemCount = list.size
            exam_by_cycles_recycler_view.setRecycledViewPool(pool)
            adapter.submitList(list)
        }
    }

}