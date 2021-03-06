package com.doubleb.meusemestre.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.ui.viewholders.RegisterGradesViewHolder

class RegisterGradesAdapter : RecyclerView.Adapter<RegisterGradesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RegisterGradesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.vh_register_grades, parent, false)
        )

    override fun onBindViewHolder(holder: RegisterGradesViewHolder, position: Int) {}

    override fun getItemCount() = 1
}