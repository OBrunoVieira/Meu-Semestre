package com.doubleb.meusemestre.ui.activities

import android.os.Bundle
import com.doubleb.meusemestre.BuildConfig
import com.doubleb.meusemestre.R
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity(R.layout.activity_profile) {

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
    }
}