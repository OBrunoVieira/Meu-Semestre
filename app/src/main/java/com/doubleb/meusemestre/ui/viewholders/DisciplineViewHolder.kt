package com.doubleb.meusemestre.ui.viewholders

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.roundWhenBase10
import com.doubleb.meusemestre.models.Discipline
import kotlinx.android.synthetic.main.vh_discipline.view.*

class DisciplineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: Discipline) {
        itemView.run {
            discipline_text_view_title.text = item.name
            discipline_text_view_average_title.text = transformGradeToText(item.grade)

            discipline_card_view.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    if (bindingAdapterPosition % 2 == 0) R.color.light_purple
                    else R.color.light_blue
                )
            )

            discipline_image_view_wave.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    if (bindingAdapterPosition % 2 == 0) R.drawable.vector_wave_left
                    else R.drawable.vector_wave_right
                )
            )

            discipline_progress_average.progress = ((item.grade / 10) * 100).toInt()
        }
    }

    private fun transformGradeToText(grade: Float) = run {
        HtmlCompat.fromHtml(
            itemView.context.getString(R.string.discipline_average, grade.roundWhenBase10()),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

}