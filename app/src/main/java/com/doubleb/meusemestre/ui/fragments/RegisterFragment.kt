package com.doubleb.meusemestre.ui.fragments

import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.tintBackgroundColorRes
import com.google.android.material.button.MaterialButton

abstract class RegisterFragment(@LayoutRes contentLayout: Int) : Fragment(contentLayout),
    View.OnClickListener {

    private val buttons: Array<MaterialButton> by lazy { buttonList() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttons.forEach { it.setOnClickListener(this) }
    }

    override fun onClick(view: View) {
        val materialButton = view as MaterialButton
        listener().onOptionClick(materialButton, type())

        buttons.forEach {
            it.tintBackgroundColorRes(android.R.color.transparent)
            it.strokeColor = ContextCompat.getColorStateList(view.context, R.color.zircon)
        }

        materialButton.tintBackgroundColorRes(R.color.light_yellow)
        materialButton.startAnimation(AlphaAnimation(.5f, 1f).apply { duration = 150 })

        materialButton.strokeColor =
            ContextCompat.getColorStateList(view.context, android.R.color.transparent)
    }

    abstract fun buttonList(): Array<MaterialButton>

    abstract fun type(): Type

    abstract fun listener(): Listener

    interface Listener {
        fun onOptionClick(button: MaterialButton, type: Type)
        fun onInstitutionSelected(value: String) {}
        fun onExamsNumberSelected(value: Int?) {}
        fun onApprovalAverageSelected(value: Double?) {}
    }

    enum class Type {
        Institution,
        Exams,
        Average
    }

}