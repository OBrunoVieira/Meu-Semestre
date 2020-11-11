package com.doubleb.meusemestre.ui.adapters.viewpager

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.ui.fragments.WelcomeInfoFragment

class WelcomePageAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = 4
    override fun createFragment(position: Int) =
        when (position) {
            0 ->
                WelcomeInfoFragment.newInstance(
                    R.drawable.shape_profile,
                    R.string.welcome_info_first_title,
                    R.string.welcome_info_first_description
                )

            1 ->
                WelcomeInfoFragment.newInstance(
                    R.drawable.shape_profile,
                    R.string.welcome_info_second_title,
                    R.string.welcome_info_second_description
                )

            2 ->
                WelcomeInfoFragment.newInstance(
                    R.drawable.shape_profile,
                    R.string.welcome_info_third_title,
                    R.string.welcome_info_third_description
                )

            else ->
                WelcomeInfoFragment.newInstance(
                    R.drawable.shape_profile,
                    R.string.welcome_info_fourth_title,
                    R.string.welcome_info_fourth_description
                )
        }
}