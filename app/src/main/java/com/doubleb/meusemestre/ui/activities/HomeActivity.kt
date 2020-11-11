package com.doubleb.meusemestre.ui.activities

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.ui.fragments.DashboardFragment
import com.doubleb.meusemestre.ui.fragments.DisciplinesFragment
import com.doubleb.meusemestre.ui.views.BottomNavigation
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity(R.layout.activity_home) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflateDashboard()

        home_bottom_navigation.setListener { _, type ->
            home_fab.isVisible = type == BottomNavigation.Type.DISCIPLINES

            when (type) {
                BottomNavigation.Type.DASHBOARD -> {
                    inflateDashboard()
                }

                BottomNavigation.Type.DISCIPLINES -> {
                    home_text_view_title.setText(R.string.home_disciplines)
                    inflateFragment(DisciplinesFragment())
                }

                else -> {
                    home_text_view_title.setText(R.string.home_tips)
                    inflateFragment(DashboardFragment())
                }
            }
        }
    }

    private fun inflateFragment(fragment: Fragment) {
        inflateFragment(R.id.home_fragment_container, fragment)
    }

    private fun inflateDashboard() {
        home_text_view_title.setText(R.string.home_dashboard)
        inflateFragment(DashboardFragment())
    }

}