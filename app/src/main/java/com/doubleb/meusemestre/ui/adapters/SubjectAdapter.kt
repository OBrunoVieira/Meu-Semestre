package com.doubleb.meusemestre.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Subject
import com.doubleb.meusemestre.ui.adapters.diff.SubjectDiffUtils
import com.doubleb.meusemestre.ui.viewholders.SubjectViewHolder

class SubjectAdapter : ListAdapter<Subject, SubjectViewHolder>(SubjectDiffUtils()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SubjectViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.vh_subject, parent, false)
        )

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
