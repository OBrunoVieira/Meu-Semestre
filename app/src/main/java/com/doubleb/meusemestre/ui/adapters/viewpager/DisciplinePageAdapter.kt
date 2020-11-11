package com.doubleb.meusemestre.ui.adapters.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.doubleb.meusemestre.ui.fragments.ActiveSemesterFragment
import com.doubleb.meusemestre.ui.fragments.SemesterHistoryFragment

class DisciplinePageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int) =
        when (position) {
            0 -> ActiveSemesterFragment()
            else -> SemesterHistoryFragment()
        }
}