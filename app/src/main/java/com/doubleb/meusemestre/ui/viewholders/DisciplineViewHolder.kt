package com.doubleb.meusemestre.ui.viewholders

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.extensions.loadImage
import com.doubleb.meusemestre.extensions.swipeColorByPosition
import com.doubleb.meusemestre.extensions.swipeWaveByPosition
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.models.Exam
import com.doubleb.meusemestre.models.extensions.hasPendingGrades
import com.doubleb.meusemestre.ui.listeners.DisciplineListener
import kotlinx.android.synthetic.main.vh_discipline.view.*

class DisciplineViewHolder(itemView: View, listener: DisciplineListener?) :
    RecyclerView.ViewHolder(itemView) {

    init {
        itemView.discipline_card_view.setOnClickListener {
            listener?.onDisciplineClick(bindingAdapterPosition)
        }
    }

    fun bind(item: Discipline, exams: List<Exam>?) {
        itemView.run {
            discipline_image_view_warning.isVisible = exams?.hasPendingGrades() == true

            discipline_card_view.contentDescription =
                discipline_average_indicator.recoverContentDescription(item.name, item.average)

            discipline_average_indicator.title(item.name)
            discipline_average_indicator.average(item.average)

            discipline_image_view_knowledge_area.loadImage(item.image)

            discipline_image_view_wave.background =
                context.swipeWaveByPosition(bindingAdapterPosition)

            discipline_card_view.setCardBackgroundColor(
                context.swipeColorByPosition(bindingAdapterPosition)
            )
        }
    }

}