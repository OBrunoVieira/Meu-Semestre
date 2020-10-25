package com.doubleb.meusemestre.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.models.Subject
import com.doubleb.meusemestre.ui.adapters.SubjectAdapter
import kotlinx.android.synthetic.main.vh_subject_list.view.*

class SubjectListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val subjectsAdapter by lazy { SubjectAdapter() }

    init {
        itemView.subject_list_recycler_view.adapter = subjectsAdapter
    }

    fun bind(list: List<Subject>) {
        subjectsAdapter.submitList(list)
    }

}