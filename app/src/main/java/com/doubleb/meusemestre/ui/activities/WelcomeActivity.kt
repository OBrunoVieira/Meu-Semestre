package com.doubleb.meusemestre.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.blendColorAnimation
import com.doubleb.meusemestre.ui.adapters.viewpager.WelcomeAdapter
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : BaseActivity(R.layout.activity_welcome) {
    private var previousPosition = 0

    private val colors by lazy {
        arrayOf(
            R.color.dark_purple,
            R.color.dark_blue,
            R.color.dark_yellow
        )
    }

    private val pageChangeCallback by lazy { onPageChangeCallback() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        welcome_view_pager.apply {
            adapter = WelcomeAdapter(this@WelcomeActivity)
            welcome_page_indicator.count = (adapter as WelcomeAdapter).itemCount
            registerOnPageChangeCallback(pageChangeCallback)
        }

        welcome_button_explore.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        welcome_button_login.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        welcome_view_pager.unregisterOnPageChangeCallback(pageChangeCallback)
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
}