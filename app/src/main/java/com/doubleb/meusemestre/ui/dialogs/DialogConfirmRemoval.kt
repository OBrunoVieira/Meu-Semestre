package com.doubleb.meusemestre.ui.dialogs

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.doubleb.meusemestre.R
import kotlinx.android.synthetic.main.dialog_confirm_removal.*


class DialogConfirmRemoval : DialogFragment(R.layout.dialog_confirm_removal) {
    companion object {
        const val CONFIRM_REMOVAL_TAG = "CONFIRM_REMOVAL_TAG"
    }

    private var title: String? = null
    private var type: Type? = null
    private var listener: View.OnClickListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        title?.let { confirm_removal_text_title.text = it }

        type?.let {
            confirm_removal_text_description.setText(when (it) {
                Type.DISCIPLINE -> R.string.confirm_removal_discipline_text
                Type.EXAM -> R.string.confirm_removal_exam_text
            })
        }

        listener?.let {
            confirm_removal_button_exclude.setOnClickListener { view ->
                it.onClick(view)
                dismiss()
            }
        }

        confirm_removal_button_cancel.setOnClickListener { dismiss() }

        dialog?.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun getTheme() = R.style.Theme_Dialog

    fun title(title: String?) = apply {
        this.title = title
    }

    fun type(type: Type) = apply {
        this.type = type
    }

    fun listener(listener: View.OnClickListener) = apply {
        this.listener = listener
    }

    internal fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, CONFIRM_REMOVAL_TAG)
    }

    override fun dismiss() {
        if(dialog?.isShowing == true) {
            super.dismiss()
        }
    }

    enum class Type {
        DISCIPLINE,
        EXAM
    }
}