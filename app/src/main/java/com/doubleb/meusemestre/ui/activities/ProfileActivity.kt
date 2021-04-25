package com.doubleb.meusemestre.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.doubleb.meusemestre.BuildConfig
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.loadRoundedImage
import com.doubleb.meusemestre.extensions.setTextIfValid
import com.doubleb.meusemestre.models.User
import com.doubleb.meusemestre.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_profile.*
import org.koin.android.ext.android.inject

class ProfileActivity : BaseActivity(R.layout.activity_profile) {

    companion object {
        private const val USER_INFO_EXTRA = "USER_INFO_EXTRA"

        fun newInstance(activity: Activity, user: User?) = activity.startActivity(
            Intent(activity, ProfileActivity::class.java)
                .putExtra(USER_INFO_EXTRA, user)
        )
    }

    //region immutable vars

    //region viewModels
    private val loginViewModel: LoginViewModel by inject()
    //endregion

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
    //endregion

    //region mutable vars
    private var user: User? = null
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = intent.getParcelableExtra(USER_INFO_EXTRA)

        setSupportActionBar(profile_toolbar)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
        }

        profile_textview_name.setTextIfValid(user?.name)
        profile_imageview.loadRoundedImage( user?.picture)

        profile_textview_version.text =
            getString(R.string.profile_version_title, BuildConfig.VERSION_NAME)

        profile_textview_notification.setOnClickListener {
            profile_switch_notification.isChecked = !profile_switch_notification.isChecked
        }

        profile_textview_logout.setOnClickListener(logoutClickListener)
        profile_imageview_logout.setOnClickListener(logoutClickListener)
    }
}