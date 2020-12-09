package com.doubleb.meusemestre.ui.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.ui.viewholders.EmptySemesterViewHolder
import com.doubleb.meusemestre.ui.views.EmptyStateView

class EmptySemesterAdapter(val listener: EmptyStateView.ClickListener) : RecyclerView.Adapter<EmptySemesterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EmptySemesterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.vh_empty_semester, parent, false),
            listener
        )

    override fun onBindViewHolder(holder: EmptySemesterViewHolder, position: Int) {}

    override fun getItemCount() = 1
}