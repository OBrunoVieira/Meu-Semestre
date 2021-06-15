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
import com.doubleb.meusemestre.ui.dialogs.DialogConfirmRemoval
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
        const val EXTRA_IS_ON_EDIT_MODE = "EXTRA_IS_ON_EDIT_MODE"
        const val EXTRA_EXAM = "EXTRA_EXAM"
    }

    //region immutable vars

    //region viewmodels
    private val examViewModel: ExamsViewModel by inject()
    //endregion

    //region components
    private val dialogConfirmRemoval by lazy { DialogConfirmRemoval().type(DialogConfirmRemoval.Type.EXAM) }
    //endregion

    private val cyclesList by lazy {
        (1..cycles).map { getCycle(it) }
    }

    private val dropdownAdapter by lazy {
        ArrayAdapter(this, R.layout.dropdown_menu_item, cyclesList)
    }
    //endregion

    //region mutable vars
    private var disciplineId: String = ""
    private var exam: Exam? = null
    private var cycles: Int = 0
    private var isOnEditMode: Boolean = false
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(exam_registration_toolbar)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        disciplineId = intent.extras?.getString(EXTRA_DISCIPLINE_ID).orEmpty()
        exam = intent.extras?.getParcelable(EXTRA_EXAM)
        cycles = intent.extras?.getInt(EXTRA_CYCLES) ?: 0
        isOnEditMode = intent.extras?.getBoolean(EXTRA_IS_ON_EDIT_MODE) == true

        examViewModel.liveDataExamCreation.observe(this, observeExamCreation())
        examViewModel.liveDataExamUpdate.observe(this, observeExamUpdate())
        examViewModel.liveDataExamRemoval.observe(this, observeExamRemoval())

        exam?.let { exam ->
            exam_registration_auto_complete_cycles.setText(getCycle(exam.cycle))
            exam.grade_value?.let { exam_registration_edit_text_grade_value.setText(it.toString()) }
            exam.grade_result?.let { exam_registration_edit_text_grade_result.setText(it.toString()) }
            exam.name?.let { exam_registration_edit_text_name.setText(it) }

            runActionButtonValidation()
        }

        addListeners()
        configureAutoCompleteCycles()
        configureActionButton()
    }

    override fun onDestroy() {
        super.onDestroy()
        dialogConfirmRemoval.dismiss()
    }

    override fun onPrepareOptionsMenu(menu: Menu?) = run {
        menu?.findItem(R.id.menu_exam_registration_delete)?.isVisible = isOnEditMode
        true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menuInflater.inflate(R.menu.menu_exam_registration, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_exam_registration_delete) {
            dialogConfirmRemoval
                .title(exam_registration_edit_text_name.text.toString())
                .listener {
                    examViewModel.removeExam(disciplineId, exam?.id.orEmpty())
                }
                .show(supportFragmentManager)
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

    private fun observeExamUpdate() = Observer<DataSource<Exam>> {
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

    private fun observeExamRemoval() = Observer<DataSource<Exam>> {
        when (it.dataState) {
            DataState.LOADING -> {
                //TODO TRATAR LOADING AO TENTAR REMOVER UMA DISCIPLINA
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
        exam_registration_edit_text_grade_result.disable()
        exam_registration_edit_text_grade_value.disable()
        exam_registration_edit_text_name.disable()
    }

    private fun buildExamErrorState() {
        exam_registration_button.state(ActionButtonView.State.DEFAULT)
        exam_registration_edit_text_grade_result.enable()
        exam_registration_edit_text_grade_value.enable()
        exam_registration_edit_text_name.enable()
    }

    private fun runActionButtonValidation() = run {
        val isGradeValidOnEditMode =
            exam_registration_edit_text_grade_result.text.isValid() && isOnEditMode

        exam_registration_button.isEnabled(
            exam_registration_auto_complete_cycles.editableText.isValid() &&
                    exam_registration_edit_text_grade_value.text.isValid() &&
                    exam_registration_edit_text_name.text.isValid() &&
                    (isGradeValidOnEditMode || !isOnEditMode)
        )
    }

    private fun addListeners() {
        exam_registration_edit_text_grade_result.addTextChangedListener(
            onTextChanged = { _, _, _, _ -> runActionButtonValidation() }
        )

        exam_registration_edit_text_grade_value.addTextChangedListener(
            onTextChanged = { _, _, _, _ -> runActionButtonValidation() }
        )

        exam_registration_edit_text_name.addTextChangedListener(
            onTextChanged = { _, _, _, _ -> runActionButtonValidation() }
        )

        exam_registration_auto_complete_cycles.setOnItemClickListener { _, _, _, _ ->
            runActionButtonValidation()
        }
    }

    private fun configureAutoCompleteCycles() = exam_registration_auto_complete_cycles.run {
        showSoftInputOnFocus = false
        disableCopyPaste()
        setAdapter(dropdownAdapter)
        setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                showDropDown()
                view.hideKeyboard()
            }
        }
    }

    private fun configureActionButton() = exam_registration_button.run {
        isEnabled(false)
        listener {
            val cycle =
                cyclesList
                    .indexOf(exam_registration_auto_complete_cycles.editableText.toString())
                    .inc()

            if (isOnEditMode) {
                examViewModel.updateExam(
                    disciplineId,
                    exam?.id,
                    exam_registration_edit_text_name.text.toString(),
                    cycle,
                    exam_registration_edit_text_grade_value.text.toString().toFloatOrNull(),
                    exam_registration_edit_text_grade_result.text.toString().toFloatOrNull()
                )
                return@listener
            }

            examViewModel.createExam(
                disciplineId,
                exam_registration_edit_text_name.text.toString(),
                cycle,
                exam_registration_edit_text_grade_value.text.toString().toFloatOrNull(),
                exam_registration_edit_text_grade_result.text.toString().toFloatOrNull())
        }
    }

    private fun getCycle(cycle: Int?) =
        getString(R.string.exam_registration_cycles_value, cycle ?: 1)
}