package com.doubleb.meusemestre.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.models.Subject
import kotlinx.android.synthetic.main.vh_best_subject.view.*

class BestSubjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(subject: Subject) {
        itemView.run {
            best_subject_text_view_name.text = subject.name
            best_subject_circle_chart.grade(subject.grade).build()
        }
    }

}