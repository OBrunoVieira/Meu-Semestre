package com.doubleb.meusemestre.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ConcatAdapter
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.createActivityResultLauncher
import com.doubleb.meusemestre.extensions.isValid
import com.doubleb.meusemestre.extensions.launchActivity
import com.doubleb.meusemestre.models.Dashboard
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.ui.activities.DisciplineRegistrationActivity
import com.doubleb.meusemestre.ui.activities.DisciplineRegistrationActivity.Companion.CURRENT_SEMESTER_EXTRA
import com.doubleb.meusemestre.ui.activities.HomeActivity
import com.doubleb.meusemestre.ui.adapters.recyclerview.*
import com.doubleb.meusemestre.ui.dialogs.BottomSheetSemesterRegistration
import com.doubleb.meusemestre.ui.listeners.DisciplineListener
import com.doubleb.meusemestre.ui.listeners.RegisterGradesListener
import com.doubleb.meusemestre.ui.views.EmptyStateView
import com.doubleb.meusemestre.viewmodel.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.koin.android.ext.android.inject

class DashboardFragment : Fragment(R.layout.fragment_dashboard),
    DisciplineListener,
    BottomSheetSemesterRegistration.SemesterRegistrationClickListener, RegisterGradesListener {

    //region immutable vars

    //region adapters
    private val disciplineListAdapter by lazy { DisciplineListAdapter() }
    private val bestDisciplineAdapter by lazy { BestDisciplineAdapter() }
    private val restrictedDisciplineListAdapter by lazy { RestrictedDisciplineListAdapter() }
    private val registerGradesAdapter by lazy { RegisterGradesAdapter() }
    private val finishSemesterAdapter by lazy { FinishSemesterAdapter() }
    private val gradeHighlightAdapter by lazy { GradeHighlightAdapter() }
    private val loadingAdapter by lazy { LoadingAdapter() }
    private val emptySemesterAdapter by lazy { EmptySemesterAdapter(getEmptySemesterListener()) }
    private val emptyDisciplinesAdapter by lazy {
        EmptyDisciplinesAdapter(getEmptyDisciplinesListener())
    }

    private val concatAdapter by lazy { ConcatAdapter(loadingAdapter) }
    //endregion

    //region components
    private val homeActivity by lazy { (activity as? HomeActivity) }
    private val bottomSheet by lazy { BottomSheetSemesterRegistration(this) }
    //endregion

    //region viewmodels
    private val dashboardViewModel: DashboardViewModel by inject()
    private val disciplinesViewModel: DisciplinesViewModel by inject()
    private val semesterViewModel: SemesterViewModel by inject()
    //endregion

    //endregion

    //region mutable vars
    private var disciplines: List<Discipline>? = null

    private lateinit var disciplineRegistrationCallback: ActivityResultLauncher<Intent>
    //endregion

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disciplineRegistrationCallback = createActivityResultLauncher {
            if (it.resultCode == RESULT_OK) {
                disciplinesViewModel.getDisciplines()
            }
        }

        setFragmentResultListener(DisciplineDetailsFragment.REQUEST_SUCCESS) { _, _ ->
            disciplinesViewModel.getDisciplines()
        }

        semesterViewModel.livedata
            .observe(viewLifecycleOwner, observeSemesterCreation())

        dashboardViewModel.liveData
            .observe(viewLifecycleOwner, observeDashboard())

        disciplinesViewModel.liveDataDiscipline
            .observe(viewLifecycleOwner, observeDisciplinesRecovery())

        dashboard_recycler_view.adapter = concatAdapter
        disciplineListAdapter.listener = this
        registerGradesAdapter.listener = this

        if (disciplineListAdapter.list.isNullOrEmpty()) {
            dashboardViewModel.getDashboard()
        }
    }
    //endregion

    //region listeners
    override fun onDisciplineClick(position: Int) {
        (activity as? HomeActivity)?.inflateStackFragment(
            DisciplineDetailsFragment.instance(
                disciplineListAdapter.list?.get(position)?.id,
                disciplineListAdapter.list?.get(position)?.name,
                disciplineListAdapter.list?.get(position)?.grade
            )
        )
    }

    override fun onDisciplineDelete(position: Int) {}

    override fun onRegisterGrade() {
        (activity as? HomeActivity)?.selectDisciplines()
    }

    private fun getEmptySemesterListener() = EmptyStateView.ClickListener {
        bottomSheet.show(childFragmentManager, BottomSheetSemesterRegistration.TAG)
    }

    private fun getEmptyDisciplinesListener() = EmptyStateView.ClickListener {
        disciplineRegistrationCallback.launchActivity<DisciplineRegistrationActivity>(
            context, CURRENT_SEMESTER_EXTRA to homeActivity?.user?.current_semester
        )
    }

    //region semester creation
    override fun onCreateSemester(name: String) {
        semesterViewModel.createSemester(name)
    }
    //endregion

    //endregion

    //region observers
    private fun observeSemesterCreation() = Observer<DataSource<String>> {
        when (it.dataState) {
            DataState.LOADING -> {

            }

            DataState.SUCCESS -> {
                homeActivity?.user = homeActivity?.user?.copy(current_semester = it.data)
                bottomSheet.dismiss()
                buildEmptyDisciplinesState()
            }

            DataState.ERROR -> {
                bottomSheet.dismiss()
            }
        }
    }

    private fun observeDashboard() = Observer<DataSource<Dashboard>> {
        when (it.dataState) {
            DataState.LOADING -> {
            }

            DataState.SUCCESS -> {
                this.disciplines = it.data?.disciplines
                homeActivity?.user = it.data?.user
                buildDashboard()
            }

            DataState.ERROR -> {
            }
        }
    }

    private fun observeDisciplinesRecovery() = Observer<DataSource<List<Discipline>>> {
        when (it.dataState) {
            DataState.LOADING -> {
                buildFullLoadingState()
            }

            DataState.SUCCESS -> {
                addDisciplines(it.data)
            }

            DataState.ERROR -> {
            }
        }
    }
    //endregion

    private fun buildDashboard() {
        when {
            !disciplines.isNullOrEmpty() -> {
                addDisciplines(disciplines)
            }

            homeActivity?.user?.current_semester.isValid() -> {
                buildEmptyDisciplinesState()
            }

            else -> {
                buildEmptySemesterState()
            }
        }
    }

    private fun buildFullLoadingState() {
        concatAdapter.removeAdapter(emptyDisciplinesAdapter)
        concatAdapter.removeAdapter(emptySemesterAdapter)
        concatAdapter.removeAdapter(registerGradesAdapter)
        concatAdapter.removeAdapter(disciplineListAdapter)

        concatAdapter.addAdapter(loadingAdapter)
    }

    private fun buildEmptyDisciplinesState() {
        concatAdapter.removeAdapter(loadingAdapter)
        concatAdapter.removeAdapter(emptySemesterAdapter)
        concatAdapter.removeAdapter(registerGradesAdapter)
        concatAdapter.removeAdapter(disciplineListAdapter)

        concatAdapter.addAdapter(emptyDisciplinesAdapter)
    }

    private fun buildEmptySemesterState() {
        concatAdapter.removeAdapter(loadingAdapter)
        concatAdapter.removeAdapter(emptyDisciplinesAdapter)
        concatAdapter.removeAdapter(registerGradesAdapter)
        concatAdapter.removeAdapter(disciplineListAdapter)

        concatAdapter.addAdapter(emptySemesterAdapter)
    }

    private fun addDisciplines(list: List<Discipline>?) {
        concatAdapter.removeAdapter(loadingAdapter)
        concatAdapter.removeAdapter(emptyDisciplinesAdapter)
        concatAdapter.removeAdapter(emptySemesterAdapter)

        disciplineListAdapter.list = list
        concatAdapter.addAdapter(disciplineListAdapter)
        concatAdapter.addAdapter(registerGradesAdapter)
    }
}