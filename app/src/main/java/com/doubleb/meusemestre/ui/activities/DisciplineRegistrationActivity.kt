package com.doubleb.meusemestre.ui.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.disableCopyPaste
import com.doubleb.meusemestre.extensions.hideKeyboard
import kotlinx.android.synthetic.main.activity_discipline_registration.*

class DisciplineRegistrationActivity : BaseActivity(R.layout.activity_discipline_registration) {

    val adapter by lazy {
        ArrayAdapter(
            this,
            R.layout.dropdown_menu_item,
            resources.getStringArray(R.array.discipline_registration_knowledge_areas)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(discipline_registration_toolbar)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        discipline_registration_auto_complete_text_view.showSoftInputOnFocus = false
        discipline_registration_auto_complete_text_view.disableCopyPaste()
        discipline_registration_auto_complete_text_view.setAdapter(adapter)
        discipline_registration_auto_complete_text_view.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                discipline_registration_auto_complete_text_view.showDropDown()
                view.hideKeyboard()
            }
        }
    }
}