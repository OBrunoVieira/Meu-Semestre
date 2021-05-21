package com.doubleb.meusemestre.ui.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.ui.listeners.RegisterGradesListener
import com.doubleb.meusemestre.ui.viewholders.RegisterGradesViewHolder

class RegisterGradesAdapter(var listener: RegisterGradesListener? = null) :
    RecyclerView.Adapter<RegisterGradesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RegisterGradesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.vh_register_grades, parent, false),
            listener
        )

    override fun onBindViewHolder(holder: RegisterGradesViewHolder, position: Int) {}

    override fun getItemCount() = 1
}