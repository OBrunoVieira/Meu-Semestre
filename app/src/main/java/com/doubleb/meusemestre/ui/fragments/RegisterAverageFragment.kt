package com.doubleb.meusemestre.ui.fragments

import com.doubleb.meusemestre.R
import kotlinx.android.synthetic.main.fragment_register_average.*

class RegisterAverageFragment(private val listener: Listener) :
    RegisterFragment(R.layout.fragment_register_average) {

    override fun buttonList() = arrayOf(
        register_average_button_5,
        register_average_button_6,
        register_average_button_7,
        register_average_button_8,
        register_average_button_9
    )

    override fun type() = Type.Average

    override fun listener() = listener
}