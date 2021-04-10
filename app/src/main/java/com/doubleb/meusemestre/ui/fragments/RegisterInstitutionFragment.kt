package com.doubleb.meusemestre.ui.fragments

import android.view.View
import com.doubleb.meusemestre.R
import kotlinx.android.synthetic.main.fragment_register_insitution.*

class RegisterInstitutionFragment(private val listener: Listener) :
    RegisterFragment(R.layout.fragment_register_insitution) {

    override fun buttonList() =
        arrayOf(register_institution_button_public, register_institution_button_private)

    override fun type() = Type.Institution

    override fun listener() = listener

    override fun onClick(view: View) {
        super.onClick(view)

        val value = if (view.id == R.id.register_institution_button_public) {
            R.string.register_institution_public_tag
        } else {
            R.string.register_institution_private_tag
        }

        listener().onInstitutionSelected(getString(value))
    }
}