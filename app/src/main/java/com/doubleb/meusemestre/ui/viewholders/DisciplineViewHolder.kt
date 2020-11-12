package com.doubleb.meusemestre.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.extensions.swipeColorByPosition
import com.doubleb.meusemestre.extensions.swipeWaveByPosition
import com.doubleb.meusemestre.models.Discipline
import kotlinx.android.synthetic.main.vh_discipline.view.*

class DisciplineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: Discipline) {
        itemView.run {
            discipline_text_view_title.text = item.name
            discipline_average_indicator.average(item.grade)

            discipline_image_view_wave.background =
                context.swipeWaveByPosition(bindingAdapterPosition)

            discipline_card_view.setCardBackgroundColor(
                context.swipeColorByPosition(bindingAdapterPosition)
            )
        }
    }

}