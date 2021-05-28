package com.doubleb.meusemestre.ui.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.*
import com.doubleb.meusemestre.models.Exam
import com.doubleb.meusemestre.ui.views.ActionButtonView
import com.doubleb.meusemestre.viewmodel.DataSource
import com.doubleb.meusemestre.viewmodel.DataState
import com.doubleb.meusemestre.viewmodel.ExamsViewModel
import kotlinx.android.synthetic.main.activity_exam_registration.*
import org.koin.android.ext.android.inject

class ExamRegistrationActivity : BaseActivity(R.layout.activity_exam_registration) {

    companion object {
        const val EXTRA_DISCIPLINE_ID = "EXTRA_DISCIPLINE_ID"
        const val EXTRA_CYCLES = "EXTRA_CYCLES"
    }

    //region immutable vars

    //region viewmodels
    private val examViewModel: ExamsViewModel by inject()
    //endregion

    private val cyclesList by lazy {
        (1..cycles).map { getString(R.string.exam_registration_cycles_value, it) }
    }

    private val adapter by lazy {
        ArrayAdapter(this, R.layout.dropdown_menu_item, cyclesList)
    }
    //endregion

    //region mutable vars
    private var disciplineId: String = ""
    private var cycles: Int = 0
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(exam_registration_toolbar)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        disciplineId = intent.extras?.getString(EXTRA_DISCIPLINE_ID).orEmpty()
        cycles = intent.extras?.getInt(EXTRA_CYCLES) ?: 0

        examViewModel.liveDataExamCreation.observe(this, observeExamCreation())

        exam_registration_edit_text_grade.addTextChangedListener(
            onTextChanged = { _, _, _, _ -> runActionButtonValidation() }
        )
        exam_registration_edit_text_value.addTextChangedListener(
            onTextChanged = { _, _, _, _ -> runActionButtonValidation() }
        )
        exam_registration_edit_text_name.addTextChangedListener(
            onTextChanged = { _, _, _, _ -> runActionButtonValidation() }
        )

        exam_registration_auto_complete_cycles.setOnItemClickListener { _, _, _, _ ->
            runActionButtonValidation()
        }

        exam_registration_auto_complete_cycles.showSoftInputOnFocus = false
        exam_registration_auto_complete_cycles.disableCopyPaste()
        exam_registration_auto_complete_cycles.setAdapter(adapter)
        exam_registration_auto_complete_cycles.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                exam_registration_auto_complete_cycles.showDropDown()
                view.hideKeyboard()
            }
        }

        exam_registration_button.isEnabled(false)
        exam_registration_button.listener {
            val cycle =
                cyclesList
                    .indexOf(exam_registration_auto_complete_cycles.editableText.toString())
                    .inc()

            examViewModel.createExam(
                disciplineId,
                exam_registration_edit_text_name.text.toString(),
                cycle,
                exam_registration_edit_text_value.text.toString().toFloatOrNull(),
                exam_registration_edit_text_grade.text.toString().toFloatOrNull())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menuInflater.inflate(R.menu.menu_exam_registration, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_exam_registration_delete) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    //region observers
    private fun observeExamCreation() = Observer<DataSource<Exam>> {
        when (it.dataState) {
            DataState.LOADING -> {
                buildLoadingState()
            }

            DataState.SUCCESS -> {
                setResult(RESULT_OK)
                onBackPressed()
            }

            DataState.ERROR -> {
                buildExamErrorState()
            }
        }
    }
    //endregion

    private fun buildLoadingState() {
        exam_registration_button.state(ActionButtonView.State.LOADING)
        exam_registration_edit_text_grade.disable()
        exam_registration_edit_text_value.disable()
        exam_registration_edit_text_name.disable()
    }

    private fun buildExamErrorState() {
        exam_registration_button.state(ActionButtonView.State.DEFAULT)
        exam_registration_edit_text_grade.enable()
        exam_registration_edit_text_value.enable()
        exam_registration_edit_text_name.enable()
    }

    private fun runActionButtonValidation() =
        exam_registration_button.isEnabled(
            exam_registration_auto_complete_cycles.editableText.isValid() &&
                    exam_registration_edit_text_grade.text.isValid() &&
                    exam_registration_edit_text_value.text.isValid() &&
                    exam_registration_edit_text_name.text.isValid()
        )
}