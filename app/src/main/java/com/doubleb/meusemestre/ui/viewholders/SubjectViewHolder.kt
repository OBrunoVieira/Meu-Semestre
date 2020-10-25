package com.doubleb.meusemestre.ui.viewholders

import android.os.Build
import android.text.Html
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.roundWhenBase10
import com.doubleb.meusemestre.models.Subject
import kotlinx.android.synthetic.main.vh_subject.view.*
import java.text.DecimalFormat

class SubjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: Subject) {
        itemView.run {
            subject_text_view_title.text = item.name
            subject_text_view_average_title.text = transformGradeToText(item.grade)

            subject_card_view.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    if (bindingAdapterPosition % 2 == 0) R.color.light_purple
                    else R.color.light_blue
                )
            )

            subject_image_view_wave.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    if (bindingAdapterPosition % 2 == 0) R.drawable.vector_wave_left
                    else R.drawable.vector_wave_right
                )
            )

            subject_progress_average.progress = ((item.grade / 10) * 100).toInt()
        }
    }

    private fun transformGradeToText(grade: Float) = run {
        HtmlCompat.fromHtml(
            itemView.context.getString(R.string.subject_average, grade.roundWhenBase10()),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

}