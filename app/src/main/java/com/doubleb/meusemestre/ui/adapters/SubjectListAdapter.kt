package com.doubleb.meusemestre.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Subject
import com.doubleb.meusemestre.ui.viewholders.SubjectListViewHolder

class SubjectListAdapter(var list: List<Subject>? = null) :
    RecyclerView.Adapter<SubjectListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SubjectListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.vh_subject_list, parent, false)
        )

    override fun getItemCount() = 1

    override fun onBindViewHolder(holder: SubjectListViewHolder, position: Int) {
       list?.let { holder.bind(it) }
    }
}