package com.doubleb.meusemestre.ui.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.ui.fragments.DashboardFragment

class HomeActivity : BaseActivity(R.layout.activity_home) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflateFragment(DashboardFragment())
    }

    private fun inflateFragment(fragment: Fragment) {
        inflateFragment(R.id.home_fragment_container, fragment)
    }

}