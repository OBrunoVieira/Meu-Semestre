package com.doubleb.meusemestre.ui.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.ui.viewholders.LoadingViewHolder

class LoadingAdapter : RecyclerView.Adapter<LoadingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LoadingViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.vh_loading, parent, false),
        )

    override fun onBindViewHolder(holder: LoadingViewHolder, position: Int) {}

    override fun getItemCount() = 1
}