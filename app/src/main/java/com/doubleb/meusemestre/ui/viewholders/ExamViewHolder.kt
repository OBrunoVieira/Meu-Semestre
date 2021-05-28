package com.doubleb.meusemestre.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Exam
import kotlinx.android.synthetic.main.vh_exam.view.*

class ExamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(exam: Exam) {
        itemView.run {
            exam_text_view_title.text = exam.name
            exam_circle_chart
                .colorIndex(bindingAdapterPosition)
                .backgroundProgressColorRes(R.color.light_gray)
                .valueProportion(1.2f)
                .holeRadius(80f)
                .enableTitle(false)
                .gradeResult(exam.grade_result)
                .maxGrade(exam.grade_value)
                .build()
        }
    }

}