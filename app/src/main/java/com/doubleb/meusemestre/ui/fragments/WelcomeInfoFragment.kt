package com.doubleb.meusemestre.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.doubleb.meusemestre.R
import kotlinx.android.synthetic.main.fragment_welcome_info.*

class WelcomeInfoFragment : Fragment(R.layout.fragment_welcome_info) {

    companion object {
        private const val ARG_DRAWABLE = "ARG_DRAWABLE"
        private const val ARG_TITLE = "ARG_TITLE"
        private const val ARG_DESCRIPTION = "ARG_DESCRIPTION"

        fun newInstance(
            @DrawableRes drawable: Int,
            @StringRes title: Int,
            @StringRes description: Int
        ) =
            WelcomeInfoFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_DRAWABLE, drawable)
                    putInt(ARG_TITLE, title)
                    putInt(ARG_DESCRIPTION, description)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val drawable = arguments?.getInt(ARG_DRAWABLE) ?: R.drawable.shape_profile
        val title = arguments?.getInt(ARG_TITLE) ?: R.string.welcome_info_first_title
        val description =
            arguments?.getInt(ARG_DESCRIPTION) ?: R.string.welcome_info_first_description

        welcome_info_text_view_title.setText(title)
        welcome_info_text_view_description.setText(description)
        welcome_info_image_view.setImageDrawable(ContextCompat.getDrawable(view.context, drawable))
    }

}