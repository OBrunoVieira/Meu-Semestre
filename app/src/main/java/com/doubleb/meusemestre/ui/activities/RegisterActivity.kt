package com.doubleb.meusemestre.ui.activities

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.viewpager2.widget.ViewPager2
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.disable
import com.doubleb.meusemestre.extensions.enable
import com.doubleb.meusemestre.ui.adapters.viewpager.RegisterPageAdapter
import com.doubleb.meusemestre.ui.fragments.RegisterFragment
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity(R.layout.activity_register), RegisterFragment.Listener {

    private val pageChangeCallback by lazy { onPageChangeCallback() }
    private val registerAdapter by lazy {
        RegisterPageAdapter(
            this@RegisterActivity,
            this@RegisterActivity
        )
    }
    private var buttonMap: HashMap<String, Boolean> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        register_view_pager.apply {
            isUserInputEnabled = false
            adapter = registerAdapter
            registerOnPageChangeCallback(pageChangeCallback)
        }

        register_button_proceed.disable()
        register_button_proceed.setOnClickListener {
            if (register_view_pager.currentItem < registerAdapter.itemCount - 1) {
                register_view_pager.currentItem = register_view_pager.currentItem.inc()
            } else {
                startActivity(
                    Intent(this, HomeActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
            }
        }

        register_image_view_back.setOnClickListener {
            if (register_view_pager.currentItem == 0) {
                finish()
            } else {
                register_view_pager.currentItem = register_view_pager.currentItem.dec()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        register_view_pager.unregisterOnPageChangeCallback(pageChangeCallback)
    }

    private fun onPageChangeCallback() = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            if (buttonMap[registerAdapter.type(register_view_pager.currentItem).name] == true) {
                register_button_proceed.enable()
            } else {
                register_button_proceed.disable()
            }

            register_view_pager.adapter?.let { adapter ->
                val step = position + 1.1f

                (register_progress_view.layoutParams as? LinearLayoutCompat.LayoutParams)?.let { params ->
                    val progress = step / adapter.itemCount.toFloat()

                    ValueAnimator.ofFloat(params.weight, progress).apply {
                        interpolator = OvershootInterpolator()
                        duration = 400
                        addUpdateListener {
                            params.weight = it.animatedValue as Float
                            register_progress_view.requestLayout()
                        }
                    }.start()
                }
            }
        }
    }

    override fun onOptionClick(button: MaterialButton, type: RegisterFragment.Type) {
        buttonMap[type.name] = true
        register_button_proceed.enable()
    }

}