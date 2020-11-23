package com.doubleb.meusemestre.ui.viewholders

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.ui.adapters.recyclerview.DisciplineHistoryAdapter
import kotlinx.android.synthetic.main.vh_semester_history.view.*

class SemesterHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val adapter by lazy { DisciplineHistoryAdapter() }

    private val colors by lazy {
        arrayOf(
            R.color.light_purple,
            R.color.light_blue
        )
    }

    init {
        itemView.history_recyclerview.adapter = adapter
    }

    fun bind(list: List<Discipline>, pool: RecyclerView.RecycledViewPool) {
        itemView.run {
            adapter.parentPosition = bindingAdapterPosition
            history_cardview_content.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    colors[bindingAdapterPosition % colors.size]
                )
            )

            history_textview_semester_name.text = "2020.2"

            val layoutManager = history_recyclerview.layoutManager as LinearLayoutManager
            layoutManager.initialPrefetchItemCount = list.size
            history_recyclerview.setRecycledViewPool(pool)
            adapter.submitList(list)
        }
    }

}