package com.doubleb.meusemestre.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.gone
import com.doubleb.meusemestre.ui.activities.DisciplineRegistrationActivity
import com.doubleb.meusemestre.ui.activities.HomeActivity
import com.doubleb.meusemestre.ui.adapters.viewpager.DisciplinePageAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_disciplines.*

class DisciplinesFragment : Fragment(R.layout.fragment_disciplines),
    TabLayout.OnTabSelectedListener {
    private val clickListener by lazy {
        View.OnClickListener {
            startActivity(Intent(it.context, DisciplineRegistrationActivity::class.java))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disciplines_view_pager.adapter = DisciplinePageAdapter(this)

        TabLayoutMediator(disciplines_tab_layout, disciplines_view_pager) { tab, position ->
            tab.text = resources.getStringArray(R.array.disciplines_tab_item)[position]
        }.attach()

        disciplines_tab_layout.addOnTabSelectedListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disciplines_tab_layout.removeOnTabSelectedListener(this)
    }

    override fun onStart() {
        super.onStart()
        (activity as? HomeActivity)?.fab()?.let {
            it.setOnClickListener(clickListener)
            it.setText(R.string.active_semester_fab)
            it.show()
            it.extend()
        }
    }

    override fun onStop() {
        super.onStop()
        (activity as? HomeActivity)?.fab()?.let {
            it.gone()
            it.setOnClickListener(null)
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        (activity as? HomeActivity)?.fab()?.let {
            if (tab?.position == 0) {
                it.show()
            } else {
                it.gone()
            }
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {}

    override fun onTabReselected(tab: TabLayout.Tab?) {}
}