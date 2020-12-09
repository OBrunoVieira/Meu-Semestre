package com.doubleb.meusemestre.ui.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.ui.viewholders.EmptyDisciplinesViewHolder
import com.doubleb.meusemestre.ui.views.EmptyStateView

class EmptyDisciplinesAdapter(val listener: EmptyStateView.ClickListener) : RecyclerView.Adapter<EmptyDisciplinesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EmptyDisciplinesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.vh_empty_disciplines, parent, false),
            listener
        )

    override fun onBindViewHolder(holder: EmptyDisciplinesViewHolder, position: Int) {}

    override fun getItemCount() = 1
}