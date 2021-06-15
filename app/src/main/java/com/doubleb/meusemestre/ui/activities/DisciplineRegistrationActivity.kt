package com.doubleb.meusemestre.ui.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.*
import com.doubleb.meusemestre.models.KnowledgeArea
import com.doubleb.meusemestre.ui.views.ActionButtonView
import com.doubleb.meusemestre.viewmodel.DataSource
import com.doubleb.meusemestre.viewmodel.DataState
import com.doubleb.meusemestre.viewmodel.DisciplinesViewModel
import kotlinx.android.synthetic.main.activity_discipline_registration.*
import org.koin.android.ext.android.inject

class DisciplineRegistrationActivity : BaseActivity(R.layout.activity_discipline_registration) {

    companion object {
        const val CURRENT_SEMESTER_EXTRA = "CURRENT_SEMESTER_EXTRA"
    }

    //region immutable vars
    private val disciplinesViewModel: DisciplinesViewModel by inject()
    //endregion

    //region mutable vars
    private var currentSemester: String? = null
    private var knowledgeAreas: List<KnowledgeArea>? = null
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(discipline_registration_toolbar)
        disciplinesViewModel.liveDataDisciplineCreation.observe(this, observeDisciplineCreation())
        disciplinesViewModel.liveDataKnowledgeAreas.observe(this, observeKnowledgeAreas())

        currentSemester = intent.extras?.getString(CURRENT_SEMESTER_EXTRA)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        discipline_registration_auto_complete_text_view.showSoftInputOnFocus = false
        discipline_registration_auto_complete_text_view.disableCopyPaste()
        discipline_registration_auto_complete_text_view.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                discipline_registration_auto_complete_text_view.showDropDown()
                view.hideKeyboard()
            }
        }

        discipline_registration_button.listener {
            val knowledgeName =
                discipline_registration_auto_complete_text_view.editableText.toString()

            knowledgeAreas?.find { it.name == knowledgeName}?.id?.let { knowledgeId ->
                disciplinesViewModel.createDiscipline(
                    discipline_registration_edit_text_name.text.toString(),
                    knowledgeId,
                    currentSemester
                )
            } ?: run{
                //TODO TRATAR ERRO
            }

        }

        disciplinesViewModel.getKnowledgeAreas()
    }

    //region observers
    private fun observeDisciplineCreation() = Observer<DataSource<Unit>> {
        when (it.dataState) {
            DataState.LOADING -> {
                discipline_registration_text_input_name.disable()
                discipline_registration_text_input.disable()
                discipline_registration_button.state(ActionButtonView.State.LOADING)
            }

            DataState.SUCCESS -> {
                setResult(RESULT_OK)
                onBackPressed()
            }

            DataState.ERROR -> {
                discipline_registration_text_input_name.enable()
                discipline_registration_text_input.enable()
                discipline_registration_button.state(ActionButtonView.State.DEFAULT)
            }
        }
    }

    private fun observeKnowledgeAreas() = Observer<DataSource<List<KnowledgeArea>>> { dataSource ->
        when (dataSource.dataState) {
            DataState.LOADING -> {
                discipline_registration_group.gone()
                discipline_registration_progress_bar.visible()
            }

            DataState.SUCCESS -> {
                discipline_registration_group.visible()
                discipline_registration_progress_bar.gone()

                dataSource.data
                    .takeIf { !it.isNullOrEmpty() }
                    ?.let { knowledgeAreas ->
                        this.knowledgeAreas = knowledgeAreas
                        val adapter = ArrayAdapter(
                            this,
                            R.layout.dropdown_menu_item,
                            knowledgeAreas.mapNotNull { knowledgeArea -> knowledgeArea.name }
                        )

                        discipline_registration_auto_complete_text_view.setAdapter(adapter)
                    }
            }

            DataState.ERROR -> {
                //TODO TRATAR ERRO
            }
        }
    }
    //endregion
}