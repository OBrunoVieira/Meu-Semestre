package com.doubleb.meusemestre.ui.activities

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.disable
import com.doubleb.meusemestre.extensions.enable
import com.doubleb.meusemestre.extensions.openClearedActivity
import com.doubleb.meusemestre.models.GraduationInfo
import com.doubleb.meusemestre.models.User
import com.doubleb.meusemestre.ui.adapters.viewpager.RegisterPageAdapter
import com.doubleb.meusemestre.ui.fragments.RegisterFragment
import com.doubleb.meusemestre.viewmodel.DataSource
import com.doubleb.meusemestre.viewmodel.DataState
import com.doubleb.meusemestre.viewmodel.UserViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.activity_register.*
import org.koin.android.ext.android.inject

class RegisterActivity : BaseActivity(R.layout.activity_register), RegisterFragment.Listener {

    //region immutable vars
    private val graduationInfo by lazy { GraduationInfo() }

    //region adapters
    private val pageChangeCallback by lazy { onPageChangeCallback() }
    private val registerAdapter by lazy {
        RegisterPageAdapter(
            this@RegisterActivity,
            this@RegisterActivity
        )
    }
    //endregion

    //region viewModels
    private val userViewModel: UserViewModel by inject()
    //endregion

    //endregion

    //region mutable vars
    private var buttonMap: HashMap<String, Boolean> = hashMapOf()
    //endregion

    //region lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel.liveDataGraduationInfo.observe(this, observeGraduationInfoCreation())

        register_view_pager.run {
            isUserInputEnabled = false
            adapter = registerAdapter
            registerOnPageChangeCallback(pageChangeCallback)
        }

        register_button_proceed.disable()
        register_button_proceed.setOnClickListener(onProceedClick())
        register_image_view_back.setOnClickListener(onBackClick())
    }

    override fun onDestroy() {
        super.onDestroy()
        register_view_pager.unregisterOnPageChangeCallback(pageChangeCallback)
    }
    //endregion

    //region observers
    private fun observeGraduationInfoCreation() = Observer<DataSource<User>> {
        when (it.dataState) {
            DataState.LOADING -> {
            }

            DataState.SUCCESS -> {
                openClearedActivity<HomeActivity>()
            }

            DataState.ERROR -> {
            }
        }
    }
    //endregion

    //region callbacks
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

    override fun onInstitutionSelected(value: String) {
        super.onInstitutionSelected(value)
        graduationInfo.institution_type = value
    }

    override fun onExamsNumberSelected(value: Int?) {
        super.onExamsNumberSelected(value)
        graduationInfo.cycles = value
    }

    override fun onApprovalAverageSelected(value: Double?) {
        super.onApprovalAverageSelected(value)
        graduationInfo.approval_average = value
    }

    private fun onProceedClick() = View.OnClickListener {
        if (register_view_pager.currentItem < registerAdapter.itemCount - 1) {
            register_view_pager.currentItem += 1
        } else {
            userViewModel.createGraduationInfo(graduationInfo)
        }
    }

    private fun onBackClick() = View.OnClickListener {
        if (register_view_pager.currentItem == 0) {
            finish()
        } else {
            register_view_pager.currentItem -= 1
        }
    }
    //endregion

}