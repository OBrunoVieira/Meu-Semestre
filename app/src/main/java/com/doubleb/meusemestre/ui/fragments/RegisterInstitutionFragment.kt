package com.doubleb.meusemestre.ui.fragments

import com.doubleb.meusemestre.R
import kotlinx.android.synthetic.main.fragment_register_insitution.*

class RegisterInstitutionFragment(private val listener: Listener) :
    RegisterFragment(R.layout.fragment_register_insitution) {

    override fun buttonList() =
        arrayOf(register_institution_button_public, register_institution_button_private)

    override fun type() = Type.Institution

    override fun listener() = listener
}