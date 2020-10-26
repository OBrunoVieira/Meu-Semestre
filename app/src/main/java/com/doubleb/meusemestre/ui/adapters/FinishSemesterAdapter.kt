package com.doubleb.meusemestre.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.ui.viewholders.FinishSemesterViewHolder

class FinishSemesterAdapter : RecyclerView.Adapter<FinishSemesterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FinishSemesterViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.vh_finish_semester, parent, false)
        )

    override fun onBindViewHolder(holder: FinishSemesterViewHolder, position: Int) {}

    override fun getItemCount() = 1
}