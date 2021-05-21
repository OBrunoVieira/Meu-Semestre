package com.doubleb.meusemestre.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.doubleb.meusemestre.ui.listeners.RegisterGradesListener
import kotlinx.android.synthetic.main.vh_register_grades.view.*

class RegisterGradesViewHolder(itemView: View, listener:RegisterGradesListener?) : RecyclerView.ViewHolder(itemView){

    init {
        itemView.register_grades_button.setOnClickListener { listener?.onRegisterGrade() }
    }

}