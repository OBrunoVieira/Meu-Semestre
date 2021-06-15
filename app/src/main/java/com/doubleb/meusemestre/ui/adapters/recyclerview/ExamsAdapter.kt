package com.doubleb.meusemestre.ui.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Exam
import com.doubleb.meusemestre.ui.adapters.recyclerview.diff.ExamDiffUtils
import com.doubleb.meusemestre.ui.listeners.ExamListener
import com.doubleb.meusemestre.ui.viewholders.ExamViewHolder

class ExamsAdapter(val listener: ExamListener?) :
    ListAdapter<Exam, ExamViewHolder>(ExamDiffUtils()) {

    var parentPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ExamViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.vh_exam, parent, false),
            listener
        )

    override fun onBindViewHolder(holder: ExamViewHolder, position: Int) {
        holder.bind(getItem(position), parentPosition)
    }
}