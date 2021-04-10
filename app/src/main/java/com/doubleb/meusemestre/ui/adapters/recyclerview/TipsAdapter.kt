package com.doubleb.meusemestre.ui.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Tip
import com.doubleb.meusemestre.ui.viewholders.TipsViewHolder

class TipsAdapter : RecyclerView.Adapter<TipsViewHolder>() {
    var tips: List<Tip> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TipsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.vh_tips, parent, false))

    override fun onBindViewHolder(holder: TipsViewHolder, position: Int) {
        holder.bind(tips[position])
    }

    override fun getItemCount() = tips.size
}