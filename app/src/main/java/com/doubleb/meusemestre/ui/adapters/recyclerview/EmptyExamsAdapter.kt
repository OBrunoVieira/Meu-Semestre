package com.doubleb.meusemestre.ui.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.ui.viewholders.EmptyExamsViewHolder
import com.doubleb.meusemestre.ui.views.EmptyStateView

class EmptyExamsAdapter(val listener: EmptyStateView.ClickListener) : RecyclerView.Adapter<EmptyExamsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EmptyExamsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.vh_empty_exams, parent, false),
            listener
        )

    override fun onBindViewHolder(holder: EmptyExamsViewHolder, position: Int) {}

    override fun getItemCount() = 1
}