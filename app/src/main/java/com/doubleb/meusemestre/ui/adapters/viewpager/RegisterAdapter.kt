package com.doubleb.meusemestre.ui.adapters.viewpager

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.doubleb.meusemestre.ui.fragments.RegisterAverageFragment
import com.doubleb.meusemestre.ui.fragments.RegisterExamsFragment
import com.doubleb.meusemestre.ui.fragments.RegisterFragment
import com.doubleb.meusemestre.ui.fragments.RegisterInstitutionFragment

class RegisterAdapter(
    fragmentActivity: FragmentActivity,
    private val listener: RegisterFragment.Listener
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int) =
        when (type(position)) {
            RegisterFragment.Type.Institution -> RegisterInstitutionFragment(listener)

            RegisterFragment.Type.Exams -> RegisterExamsFragment(listener)

            else -> RegisterAverageFragment(listener)
        }

    fun type(position: Int) =
        when (position) {
            0 -> RegisterFragment.Type.Institution
            1 -> RegisterFragment.Type.Exams
            else -> RegisterFragment.Type.Average
        }
}