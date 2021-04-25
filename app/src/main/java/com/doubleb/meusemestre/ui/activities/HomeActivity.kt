package com.doubleb.meusemestre.ui.activities

import android.animation.LayoutTransition.CHANGING
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.gone
import com.doubleb.meusemestre.extensions.loadRoundedImage
import com.doubleb.meusemestre.extensions.visible
import com.doubleb.meusemestre.models.User
import com.doubleb.meusemestre.ui.fragments.DashboardFragment
import com.doubleb.meusemestre.ui.fragments.DisciplinesFragment
import com.doubleb.meusemestre.ui.fragments.TipsFragment
import com.doubleb.meusemestre.ui.views.BottomNavigation
import com.doubleb.meusemestre.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.ext.android.inject

class HomeActivity : BaseActivity(R.layout.activity_home) {

    companion object {
        private const val USER_INFO_EXTRA = "USER_INFO_EXTRA"

        fun newClearedInstance(activity: Activity, user: User?) = activity.startActivity(
            Intent(activity, HomeActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .putExtra(USER_INFO_EXTRA, user)
        )
    }
    //region immutable vars

    //region viewModels
    private val userViewModel: UserViewModel by inject()
    //endregion

    //endregion

    //region mutable vars
    private var user: User? = null
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = intent.getParcelableExtra(USER_INFO_EXTRA)

        home_coordinator.layoutTransition?.enableTransitionType(CHANGING)

        home_content_profile.setOnClickListener {
            ProfileActivity.newInstance(this, user)
        }

        home_bottom_navigation.setListener { _, type ->
            home_app_bar.setExpanded(true)
            supportFragmentManager.popBackStack(
                BACK_STACK_ROOT_TAG,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )

            when (type) {
                BottomNavigation.Type.DASHBOARD -> {
                    home_text_view_title.setText(R.string.home_dashboard)
                    inflateStackFragment(DashboardFragment())
                }

                BottomNavigation.Type.DISCIPLINES -> {
                    home_text_view_title.setText(R.string.home_disciplines)
                    inflateStackFragment(DisciplinesFragment())
                }

                else -> {
                    home_text_view_title.setText(R.string.home_tips)
                    inflateStackFragment(TipsFragment())
                }
            }
        }

        home_image_view_profile.loadRoundedImage(user?.picture)
        home_bottom_navigation.selectItem(BottomNavigation.Type.DASHBOARD)
    }

    override fun onBackPressed() {
        val fragments = supportFragmentManager

        if (fragments.backStackEntryCount > 1) {
            fragments.popBackStackImmediate()
        } else {
            supportFinishAfterTransition()
        }
    }

    fun hideNavigation() {
        home_collapsing_toolbar.gone()
        home_app_bar.setExpanded(true, false)
    }

    fun showNavigation() {
        home_collapsing_toolbar.visible()
    }

    fun inflateStackFragment(fragment: Fragment) {
        inflateStackFragment(R.id.home_fragment_container, fragment)
    }

    fun fab() = home_fab

    private fun inflateFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack(
            BACK_STACK_ROOT_TAG,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        inflateFragment(R.id.home_fragment_container, fragment)
    }
}