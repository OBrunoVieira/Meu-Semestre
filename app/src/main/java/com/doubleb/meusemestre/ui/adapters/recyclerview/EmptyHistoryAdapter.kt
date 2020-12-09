package com.doubleb.meusemestre.ui.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.ui.viewholders.EmptyHistoryViewHolder

class EmptyHistoryAdapter() : RecyclerView.Adapter<EmptyHistoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EmptyHistoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.vh_empty_history, parent, false)
        )

    override fun onBindViewHolder(holder: EmptyHistoryViewHolder, position: Int) {}

    override fun getItemCount() = 1
}