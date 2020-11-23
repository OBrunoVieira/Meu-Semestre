package com.doubleb.meusemestre.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.tintBackgroundColorRes
import com.doubleb.meusemestre.models.Discipline
import kotlinx.android.synthetic.main.vh_discipline_history.view.*

class DisciplineHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val colors by lazy {
        arrayOf(
            R.color.dark_purple,
            R.color.dark_blue
        )
    }

    fun bind(discipline: Discipline, parentPosition: Int) {
        itemView.run {
            discipline_history_content.tintBackgroundColorRes(colors[parentPosition % colors.size])
            discipline_history_textview_name.text = discipline.name
            discipline_history_textview_status.text = "Aprovado"

            discipline_history_circle_chart
                .progressColorRes(R.color.zircon)
                .backgroundProgressColorRes(R.color.zircon_forty)
                .valueProportion(1.2f)
                .holeRadius(80f)
                .enableTitle(false)
                .grade(discipline.grade)
                .build()
        }
    }

}