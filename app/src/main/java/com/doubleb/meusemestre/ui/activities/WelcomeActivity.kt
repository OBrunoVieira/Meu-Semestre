package com.doubleb.meusemestre.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.blendColorAnimation
import com.doubleb.meusemestre.models.User
import com.doubleb.meusemestre.ui.adapters.viewpager.WelcomePageAdapter
import com.doubleb.meusemestre.ui.dialogs.BottomSheetLogin
import com.doubleb.meusemestre.viewmodel.DataSource
import com.doubleb.meusemestre.viewmodel.DataState
import com.doubleb.meusemestre.viewmodel.LoginViewModel
import com.doubleb.meusemestre.viewmodel.UserViewModel
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.activity_welcome.*
import org.koin.android.ext.android.inject


class WelcomeActivity : BaseActivity(R.layout.activity_welcome),
    BottomSheetLogin.LoginClickListener {

    //region immutable vars

    private val pageChangeCallback by lazy { onPageChangeCallback() }
    private val bottomSheet by lazy { BottomSheetLogin(this) }

    //region viewModels
    private val loginViewModel: LoginViewModel by inject()
    private val userViewModel: UserViewModel by inject()
    //endregion

    //endregion

    //region mutable vars
    private var previousPosition = 0

    private val colors by lazy {
        arrayOf(
            R.color.dark_purple,
            R.color.dark_blue,
            R.color.dark_yellow
        )
    }
    //endregion

    //region lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel.liveData.observe(this, observeLogin())
        userViewModel.liveDataUser.observe(this, observeUser())
        userViewModel.liveDataUserCreation.observe(this, observeUserCreation())

        welcome_view_pager.apply {
            adapter = WelcomePageAdapter(this@WelcomeActivity)
            welcome_page_indicator.count = (adapter as WelcomePageAdapter).itemCount
            registerOnPageChangeCallback(pageChangeCallback)
        }

        welcome_button_explore.setOnClickListener(onExploreClick())
        welcome_button_login.setOnClickListener(onLoginClick())

        loginViewModel.validateCurrentSession()
    }

    override fun onDestroy() {
        super.onDestroy()
        welcome_view_pager.unregisterOnPageChangeCallback(pageChangeCallback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loginViewModel.facebookCallbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
    //endregion

    //region observers
    private fun observeLogin() = Observer<DataSource<User>> {
        when (it.dataState) {
            DataState.LOADING -> {
            }

            DataState.SUCCESS -> {
                it.data?.let { user -> userViewModel.getUserOrCreate(user) }
            }

            DataState.ERROR -> {
            }
        }
    }

    private fun observeUser() = Observer<DataSource<User>> {
        when (it.dataState) {
            DataState.LOADING -> {
            }

            DataState.SUCCESS -> {
                if (it.data?.graduation_info?.isValid() == true) {
                    HomeActivity.newClearedInstance(this)
                } else {
                    startActivity(Intent(this, RegisterActivity::class.java))
                    finish()
                }
            }

            DataState.ERROR -> {
            }
        }
    }

    private fun observeUserCreation() = Observer<DataSource<User>> {
        when (it.dataState) {
            DataState.LOADING -> {
            }

            DataState.SUCCESS -> {
                startActivity(Intent(this, RegisterActivity::class.java))
                finish()
            }

            DataState.ERROR -> {
            }
        }
    }
    //endregion

    //region callbacks
    override fun onFacebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile", "email"))
    }

    override fun onGoogleLogin() {
    }

    private fun onPageChangeCallback() = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            welcome_page_indicator.selection = position

            welcome_content_main.blendColorAnimation(
                colors[previousPosition % colors.size],
                colors[position % colors.size],
                "backgroundColor"
            )

            previousPosition = position
        }
    }

    private fun onExploreClick() = View.OnClickListener {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun onLoginClick() = View.OnClickListener {
        bottomSheet.show(supportFragmentManager, BottomSheetLogin.TAG)
    }
    //endregion
}