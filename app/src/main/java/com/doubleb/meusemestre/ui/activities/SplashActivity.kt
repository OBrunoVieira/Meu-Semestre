package com.doubleb.meusemestre.ui.activities

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.openClearedActivity
import com.doubleb.meusemestre.models.User
import com.doubleb.meusemestre.viewmodel.DataSource
import com.doubleb.meusemestre.viewmodel.DataState
import com.doubleb.meusemestre.viewmodel.LoginViewModel
import com.doubleb.meusemestre.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SplashActivity : BaseActivity(R.layout.activity_splash_screen) {
    //region viewModels
    private val loginViewModel: LoginViewModel by inject()
    private val userViewModel: UserViewModel by inject()
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel.liveData.observe(this, observeLogin())
        userViewModel.liveDataUser.observe(this, observeUser())


        lifecycleScope.launch(Dispatchers.Main) {
            delay(1000)
            loginViewModel.validateCurrentSession()
        }
    }

    //region observers
    private fun observeLogin() = Observer<DataSource<User>> {
        if (it.dataState == DataState.SUCCESS) {
            userViewModel.getUser()
        } else if (it.dataState == DataState.ERROR) {
            openClearedActivity<WelcomeActivity>()
        }
    }

    private fun observeUser() = Observer<DataSource<User>> {
        if (it.dataState == DataState.SUCCESS) {
            if (it.data?.graduation_info?.isValid() == true) {
                openClearedActivity<HomeActivity>()
            } else {
                openClearedActivity<RegisterActivity>()
            }
        } else if (it.dataState == DataState.ERROR) {
            openClearedActivity<WelcomeActivity>()
        }
    }
    //endregion
}