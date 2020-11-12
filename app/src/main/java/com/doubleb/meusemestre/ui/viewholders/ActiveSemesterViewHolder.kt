package com.doubleb.meusemestre.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.extensions.swipeColorByPosition
import com.doubleb.meusemestre.extensions.swipeWaveByPosition
import com.doubleb.meusemestre.models.Discipline
import kotlinx.android.synthetic.main.vh_active_semester.view.*

class ActiveSemesterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: Discipline) {
        itemView.run {
            active_semester_text_view_title.text = item.name
            active_semester_average_indicator.average(item.grade)

            active_semester_image_view_wave.background =
                context.swipeWaveByPosition(bindingAdapterPosition)

            active_semester_card_view.setCardBackgroundColor(
                context.swipeColorByPosition(bindingAdapterPosition)
            )
        }
    }

}