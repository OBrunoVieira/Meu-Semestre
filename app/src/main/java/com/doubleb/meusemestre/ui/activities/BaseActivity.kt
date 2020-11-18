package com.doubleb.meusemestre.ui.activities

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.doubleb.meusemestre.R

open class BaseActivity(@LayoutRes contentLayoutId: Int) : AppCompatActivity(contentLayoutId) {

    companion object {
        const val BACK_STACK_ROOT_TAG = "root_fragment"
    }

    fun inflateFragment(@IdRes containerViewId: Int, fragment: Fragment) {
        val tag = fragment.javaClass.name

        if (supportFragmentManager.findFragmentByTag(tag) == null) {
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out)
                .replace(containerViewId, fragment, tag)
                .commit()
        }
    }

    fun inflateStackFragment(@IdRes containerViewId: Int, fragment: Fragment) {
        val tag = fragment.javaClass.name

        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out)
            .replace(containerViewId, fragment, tag)
            .addToBackStack(BACK_STACK_ROOT_TAG)
            .commit()
    }
}