package com.doubleb.meusemestre.ui.fragments

import com.doubleb.meusemestre.R
import kotlinx.android.synthetic.main.fragment_register_exams.*

class RegisterExamsFragment(private val listener: Listener) :
    RegisterFragment(R.layout.fragment_register_exams) {

    override fun buttonList() = arrayOf(
        register_exams_button_1,
        register_exams_button_2,
        register_exams_button_3,
        register_exams_button_4,
        register_exams_button_5,
        register_exams_button_other
    )

    override fun type() = Type.Exams

    override fun listener() = listener
}