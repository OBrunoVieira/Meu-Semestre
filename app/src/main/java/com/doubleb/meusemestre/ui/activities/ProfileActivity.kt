package com.doubleb.meusemestre.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.doubleb.meusemestre.BuildConfig
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_profile.*
import org.koin.android.ext.android.inject

class ProfileActivity : BaseActivity(R.layout.activity_profile) {

    private val loginViewModel: LoginViewModel by inject()

    private val logoutClickListener by lazy {
        View.OnClickListener {
            loginViewModel.logout {
                startActivity(
                    Intent(this, WelcomeActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(profile_toolbar)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
        }

        profile_textview_version.text =
            getString(R.string.profile_version_title, BuildConfig.VERSION_NAME)

        profile_textview_notification.setOnClickListener {
            profile_switch_notification.isChecked = !profile_switch_notification.isChecked
        }

        profile_textview_logout.setOnClickListener(logoutClickListener)
        profile_imageview_logout.setOnClickListener(logoutClickListener)
    }
}