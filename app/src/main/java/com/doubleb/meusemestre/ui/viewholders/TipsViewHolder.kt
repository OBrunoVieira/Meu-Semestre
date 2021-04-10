package com.doubleb.meusemestre.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doubleb.meusemestre.models.Tip
import kotlinx.android.synthetic.main.vh_tips.view.*

class TipsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(tip: Tip) {
        itemView.run {
            tips_text_view_title.text = tip.title
            tips_text_view_description.text = tip.description

            Glide.with(this)
                .load(tip.storage)
                .into(tips_image_view)
        }
    }
}