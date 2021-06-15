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
import com.doubleb.meusemestre.models.Exam
import com.doubleb.meusemestre.models.extensions.groupByCycle
import com.doubleb.meusemestre.models.extensions.hasPendingGrades
import com.doubleb.meusemestre.models.extensions.transformToGradeList
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
    private val gradeComparisonChartAdapter by lazy { CardGradeComparisonChartAdapter() }
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

    private val adapters by lazy {
        arrayOf(disciplineListAdapter,
            gradeComparisonChartAdapter,
            bestDisciplineAdapter,
            restrictedDisciplineListAdapter,
            registerGradesAdapter,
            finishSemesterAdapter,
            gradeHighlightAdapter,
            loadingAdapter,
            emptySemesterAdapter,
            emptyDisciplinesAdapter)
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
    private var gradeVariationByDisciplines: Map<String, Float?>? = null
    private var examsByDisciplines: Map<String, List<Exam>?>? = null

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
            dashboardViewModel.getDashboard()
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
                disciplineListAdapter.list?.get(position),
                position
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
                this.gradeVariationByDisciplines = it.data?.gradeVariationByDisciplines
                this.examsByDisciplines = it.data?.examsByDisciplines
                homeActivity?.user = it.data?.user
                buildDashboard(disciplines)
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
                buildDashboard(it.data)
            }

            DataState.ERROR -> {
            }
        }
    }
    //endregion

    private fun buildDashboard(disciplines: List<Discipline>?) {
        when {
            !disciplines.isNullOrEmpty() -> {
                removeAdapters()

                addDisciplines(disciplines)
                addChart()
                addBestDiscipline(disciplines)
                addRestrictedDisciplines(disciplines)
                addGradeHighlights(disciplines)
                addRegisterGrades()
            }

            homeActivity?.user?.current_semester.isValid() -> {
                buildEmptyDisciplinesState()
            }

            else -> {
                buildEmptySemesterState()
            }
        }
    }

    private fun addChart() {
        val examsByDisciplineName = disciplines
            ?.mapNotNull {
                val disciplineId = it.id
                val disciplineName = it.name
                val exams = examsByDisciplines
                    ?.get(disciplineId)
                    ?.filter { exam -> exam.grade_result != null }

                if (disciplineId != null && !disciplineName.isNullOrEmpty() && !exams.isNullOrEmpty()) {
                    disciplineName to exams
                } else {
                    null
                }
            }
            ?.toMap()
            ?.takeIf {
                val hasMoreThanOneGradePerCycle =
                    it.filter { entries ->
                        val gradeList = entries.value.groupByCycle()?.transformToGradeList()
                        gradeList != null && gradeList.size > 1
                    }.isNotEmpty()

                val hasMoreThanOneExamByDiscipline = it.size > 1

                hasMoreThanOneExamByDiscipline || hasMoreThanOneGradePerCycle
            }

        examsByDisciplineName?.let {
            gradeComparisonChartAdapter.examsByDisciplines = it
            concatAdapter.addAdapter(gradeComparisonChartAdapter)
        } ?: run {
            concatAdapter.removeAdapter(gradeComparisonChartAdapter)
        }
    }

    private fun addBestDiscipline(list: List<Discipline>) {
        val discipline = list
            .takeIf { it.size > 1 }
            ?.filter { it.average != null }
            ?.maxByOrNull { it.average ?: 0f }

        if (discipline != null) {
            bestDisciplineAdapter.discipline = discipline
            bestDisciplineAdapter.notifyDataSetChanged()
            concatAdapter.addAdapter(bestDisciplineAdapter)
            return
        }

        concatAdapter.removeAdapter(bestDisciplineAdapter)
    }

    private fun addRestrictedDisciplines(list: List<Discipline>) {
        val restrictedDisciplines = run {
            homeActivity?.user?.graduation_info?.approval_average?.let { approvalAverage ->
                list
                    .takeIf { it.size > 1 }
                    ?.filter {
                        val disciplineAverage = it.average
                        disciplineAverage != null && disciplineAverage < approvalAverage
                    }
                    ?.take(3)
            }
        }

        if (restrictedDisciplines?.isNotEmpty() == true) {
            restrictedDisciplineListAdapter.list = restrictedDisciplines
            restrictedDisciplineListAdapter.examsByDisciplines = examsByDisciplines
            restrictedDisciplineListAdapter.notifyDataSetChanged()
            concatAdapter.addAdapter(restrictedDisciplineListAdapter)
            return
        }

        concatAdapter.removeAdapter(restrictedDisciplineListAdapter)
    }

    private fun addGradeHighlights(list: List<Discipline>) {
        val bestDisciplineByVariation = list
            .filter {
                val gradeVariation = gradeVariationByDisciplines?.get(it.id)
                gradeVariation != null && gradeVariation > 0
            }
            .maxByOrNull { gradeVariationByDisciplines?.get(it.id) ?: 0f }

        val worstDisciplineByVariation = list
            .filter {
                val gradeVariation = gradeVariationByDisciplines?.get(it.id)
                gradeVariation != null && gradeVariation < 0
            }
            .minByOrNull { gradeVariationByDisciplines?.get(it.id) ?: 0f }

        if (bestDisciplineByVariation != null || worstDisciplineByVariation != null) {
            gradeHighlightAdapter.bestDiscipline = bestDisciplineByVariation
            gradeHighlightAdapter.worstDiscipline = worstDisciplineByVariation
            gradeHighlightAdapter.gradeVariationByDisciplines = gradeVariationByDisciplines
            gradeHighlightAdapter.notifyDataSetChanged()
            concatAdapter.addAdapter(gradeHighlightAdapter)
            return
        }

        concatAdapter.removeAdapter(gradeHighlightAdapter)
    }

    private fun addRegisterGrades() {
        val hasAnyUnregisteredGrade = examsByDisciplines?.hasPendingGrades() ?: true

        if (hasAnyUnregisteredGrade) {
            concatAdapter.addAdapter(registerGradesAdapter)
            return
        }

        concatAdapter.removeAdapter(registerGradesAdapter)
    }

    private fun buildFullLoadingState() {
        removeAdapters()
        concatAdapter.addAdapter(loadingAdapter)
    }

    private fun buildEmptyDisciplinesState() {
        removeAdapters()
        homeActivity?.disableCR()
        concatAdapter.addAdapter(emptyDisciplinesAdapter)
    }

    private fun buildEmptySemesterState() {
        removeAdapters()
        homeActivity?.disableCR()
        concatAdapter.addAdapter(emptySemesterAdapter)
    }

    private fun addDisciplines(list: List<Discipline>) {
        disciplineListAdapter.list = list
        disciplineListAdapter.examsByDisciplines = examsByDisciplines
        disciplineListAdapter.notifyDataSetChanged()
        concatAdapter.addAdapter(disciplineListAdapter)

        homeActivity?.updateCR(list.map { it.average ?: 0f })
    }

    private fun removeAdapters() = adapters.forEach { concatAdapter.removeAdapter(it) }
}