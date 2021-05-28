package com.doubleb.meusemestre.ui.activities

import android.animation.LayoutTransition.CHANGING
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.gone
import com.doubleb.meusemestre.extensions.loadRoundedImage
import com.doubleb.meusemestre.extensions.openActivity
import com.doubleb.meusemestre.extensions.visible
import com.doubleb.meusemestre.models.User
import com.doubleb.meusemestre.ui.fragments.DashboardFragment
import com.doubleb.meusemestre.ui.fragments.DisciplinesFragment
import com.doubleb.meusemestre.ui.fragments.TipsFragment
import com.doubleb.meusemestre.ui.views.BottomNavigation
import com.doubleb.meusemestre.viewmodel.DataSource
import com.doubleb.meusemestre.viewmodel.DataState
import com.doubleb.meusemestre.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.ext.android.inject

class HomeActivity : BaseActivity(R.layout.activity_home) {

    //region immutable vars

    //region viewModels
    private val userViewModel: UserViewModel by inject()
    //endregion

    //endregion

    //region mutable vars
    var user: User? = null
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel.liveDataUser.observe(this, observeUser())

        home_coordinator.layoutTransition?.enableTransitionType(CHANGING)

        home_content_profile.isEnabled = false
        home_content_profile.setOnClickListener {
            openActivity<ProfileActivity>(ProfileActivity.USER_INFO_EXTRA to user)
        }

        home_bottom_navigation.setListener { _, type ->
            expand()
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

        home_bottom_navigation.selectItem(BottomNavigation.Type.DASHBOARD)
        userViewModel.getUser()
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

    fun selectDisciplines() {
        home_bottom_navigation.selectItem(BottomNavigation.Type.DISCIPLINES)
    }

    fun expand() = home_app_bar.setExpanded(true)

    fun fab() = home_fab

    fun configureActionButton(
        @StringRes resId: Int,
        isButtonVisible: Boolean,
        listener: View.OnClickListener,
    ) {
        home_fab.setOnClickListener(listener)
        home_fab.setText(resId)

        if (isButtonVisible) {
            showFab()
        } else {
            hideFab()
        }
    }

    fun clearActionButton() {
        home_fab.gone()
        home_fab.setOnClickListener(null)
    }

    fun hideFab() = home_fab.gone()

    fun showFab() = home_fab.run {
        show()
        extend()
    }

    //region observers
    private fun observeUser() = Observer<DataSource<User>> {
        when (it.dataState) {
            DataState.LOADING -> {
                home_fragment_progress.show()
                home_fragment_container.gone()
                home_text_view_cr.gone()
            }

            DataState.SUCCESS -> {
                this.user = it.data
                home_content_profile.isEnabled = true

                home_fragment_progress.hide()
                home_fragment_container.visible()
                home_text_view_cr.visible()

                home_image_view_profile.loadRoundedImage(user?.picture)
            }

            DataState.ERROR -> {
            }
        }
    }
    //endregion

    private fun inflateFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack(
            BACK_STACK_ROOT_TAG,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )

        inflateFragment(R.id.home_fragment_container, fragment)
    }
}