package com.doubleb.meusemestre.ui.viewholders

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Exam
import com.doubleb.meusemestre.ui.listeners.ExamListener
import kotlinx.android.synthetic.main.vh_exam.view.*

class ExamViewHolder(itemView: View, listener: ExamListener? = null) :
    RecyclerView.ViewHolder(itemView) {

    private var parentPosition = 0

    init {
        itemView.setOnClickListener {
            listener?.onExamClick(parentPosition, bindingAdapterPosition)
        }
    }

    fun bind(exam: Exam, parentPosition: Int) {
        this.parentPosition = parentPosition

        itemView.run {
            exam_image_view_warning.isVisible = exam.grade_result == null

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