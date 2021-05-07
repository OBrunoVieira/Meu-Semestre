package com.doubleb.meusemestre.ui.dialogs

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.disable
import com.doubleb.meusemestre.extensions.enable
import com.doubleb.meusemestre.extensions.invisible
import com.doubleb.meusemestre.extensions.visible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_semester_registration.*

class BottomSheetSemesterRegistration(val listener: SemesterRegistrationClickListener? = null) :
    BottomSheetDialogFragment(), TextWatcher {

    companion object {
        const val TAG = "BottomSheetSemesterRegistration"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.bottom_sheet_semester_registration, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        semester_registration_button.disable()
        isCancelable = true

        semester_registration_edit_text.addTextChangedListener(this)
        semester_registration_button.setOnClickListener {
            isCancelable = false
            listener?.onCreateSemester(semester_registration_edit_text.text.toString())
            semester_registration_text_input.disable()
            semester_registration_button.invisible()
            semester_registration_loading.visible()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        semester_registration_edit_text.removeTextChangedListener(this)
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
        val shouldEnableButton = charSequence != null && charSequence.length > 3
        if (shouldEnableButton) {
            semester_registration_button.enable()
        } else {
            semester_registration_button.disable()
        }
    }

    interface SemesterRegistrationClickListener {
        fun onCreateSemester(name: String)
    }
}