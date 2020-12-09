package com.doubleb.meusemestre.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.ui.views.EmptyStateView
import kotlinx.android.synthetic.main.vh_empty_semester.view.*

class EmptySemesterViewHolder(itemView: View, val listener: EmptyStateView.ClickListener) :
    RecyclerView.ViewHolder(itemView) {

    init {
        itemView.empty_semester_state_view.listener(listener)
    }
}