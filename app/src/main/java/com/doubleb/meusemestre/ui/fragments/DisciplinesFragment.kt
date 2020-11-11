package com.doubleb.meusemestre.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.ui.adapters.viewpager.DisciplinePageAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_disciplines.*

class DisciplinesFragment : Fragment(R.layout.fragment_disciplines) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disciplines_view_pager.adapter = DisciplinePageAdapter(this)

        TabLayoutMediator(disciplines_tab_layout, disciplines_view_pager) { tab, position ->
            tab.text = resources.getStringArray(R.array.disciplines_tab_item)[position]
        }.attach()
    }
}