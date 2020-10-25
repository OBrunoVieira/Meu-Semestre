package com.doubleb.meusemestre.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Subject
import com.doubleb.meusemestre.ui.viewholders.BestSubjectViewHolder

class BestSubjectAdapter(var subject: Subject? = null) :
    RecyclerView.Adapter<BestSubjectViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BestSubjectViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.vh_best_subject, parent, false)
        )

    override fun onBindViewHolder(holder: BestSubjectViewHolder, position: Int) {
        subject?.let { holder.bind(it) }
    }

    override fun getItemCount() = 1
}