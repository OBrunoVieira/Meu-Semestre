package com.doubleb.meusemestre.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.extensions.swipeColorByPosition
import com.doubleb.meusemestre.extensions.swipeWaveByPosition
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.ui.listeners.DisciplineListener
import kotlinx.android.synthetic.main.vh_active_semester.view.*

class ActiveSemesterViewHolder(itemView: View, private val listener: DisciplineListener) :
    RecyclerView.ViewHolder(itemView) {

    init {
        itemView.active_semester_card_view.setOnClickListener {
            listener.onDisciplineClick(bindingAdapterPosition)
        }

        itemView.active_semester_image_delete.setOnClickListener {
            listener.onDisciplineDelete(bindingAdapterPosition)
        }
    }

    fun bind(item: Discipline) {
        itemView.run {
            active_semester_average_indicator.title(item.name)
            active_semester_average_indicator.average(item.grade)

            active_semester_image_view_wave.background =
                context.swipeWaveByPosition(bindingAdapterPosition)

            active_semester_card_view.setCardBackgroundColor(
                context.swipeColorByPosition(bindingAdapterPosition)
            )
        }
    }

}