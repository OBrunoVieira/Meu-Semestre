package com.doubleb.meusemestre.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doubleb.meusemestre.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_login.*

class BottomSheetLogin(val listener: LoginClickListener? = null) : BottomSheetDialogFragment() {
    companion object {
        const val TAG = "BottomSheetLogin"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.bottom_sheet_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        login_button_google.setOnClickListener {
            listener?.onGoogleLogin()
        }

        login_button_facebook.setOnClickListener {
            listener?.onFacebookLogin()
        }
    }

    interface LoginClickListener {
        fun onFacebookLogin()
        fun onGoogleLogin()
    }
}