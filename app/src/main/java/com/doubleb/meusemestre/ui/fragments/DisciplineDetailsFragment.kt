package com.doubleb.meusemestre.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ConcatAdapter
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.createActivityResultLauncher
import com.doubleb.meusemestre.extensions.launchActivity
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.models.Exam
import com.doubleb.meusemestre.ui.activities.ExamRegistrationActivity
import com.doubleb.meusemestre.ui.activities.ExamRegistrationActivity.Companion.EXTRA_CYCLES
import com.doubleb.meusemestre.ui.activities.ExamRegistrationActivity.Companion.EXTRA_DISCIPLINE_ID
import com.doubleb.meusemestre.ui.activities.HomeActivity
import com.doubleb.meusemestre.ui.adapters.recyclerview.EmptyExamsAdapter
import com.doubleb.meusemestre.ui.adapters.recyclerview.ExamsByCyclesAdapter
import com.doubleb.meusemestre.ui.adapters.recyclerview.LoadingAdapter
import com.doubleb.meusemestre.ui.views.EmptyStateView
import com.doubleb.meusemestre.viewmodel.DataSource
import com.doubleb.meusemestre.viewmodel.DataState
import com.doubleb.meusemestre.viewmodel.DisciplinesViewModel
import com.doubleb.meusemestre.viewmodel.ExamsViewModel
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_discipline_details.*
import org.koin.android.ext.android.inject
import kotlin.math.abs

class DisciplineDetailsFragment : Fragment(R.layout.fragment_discipline_details),
    EmptyStateView.ClickListener {

    companion object {
        private const val DISCIPLINE_DETAILS_ID = "DISCIPLINE_DETAILS_ID"
        private const val DISCIPLINE_DETAILS_NAME = "DISCIPLINE_DETAILS_TITLE"
        private const val DISCIPLINE_DETAILS_GRADE = "DISCIPLINE_DETAILS_GRADE"

        const val REQUEST_SUCCESS = "REQUEST_SUCCESS"

        fun instance(id: String?, title: String?, grade: Float?) =
            DisciplineDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(DISCIPLINE_DETAILS_ID, id)
                    putString(DISCIPLINE_DETAILS_NAME, title)
                    putFloat(DISCIPLINE_DETAILS_GRADE, grade ?: -1f)
                }
            }
    }

    //region immutable vars

    //region adapters
    private val examsAdapter by lazy { ExamsByCyclesAdapter() }
    private val emptyExamsAdapter by lazy { EmptyExamsAdapter(this) }
    private val loadingAdapter by lazy { LoadingAdapter() }
    private val concatAdapter by lazy { ConcatAdapter(loadingAdapter) }
    //endregion

    private val clickListener by lazy {
        View.OnClickListener { launchExamRegistrationActivity() }
    }

    //region viewmodels
    private val disciplinesViewModel: DisciplinesViewModel by inject()
    private val examsViewModel: ExamsViewModel by inject()
    //endregion

    //region components
    private val homeActivity by lazy { (activity as? HomeActivity) }
    //endregion

    //endregion

    //region mutable vars
    private var grade: Float? = null
    private var name: String? = null
    private var id: String? = null

    private lateinit var examRegistrationCallback: ActivityResultLauncher<Intent>
    //endregion

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id = arguments?.getString(DISCIPLINE_DETAILS_ID)
        name = arguments?.getString(DISCIPLINE_DETAILS_NAME)
        grade = arguments?.getFloat(DISCIPLINE_DETAILS_GRADE)

        examRegistrationCallback = createActivityResultLauncher {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                examsViewModel.getExams(id)
            }
        }

        examsViewModel.liveData.observe(viewLifecycleOwner, observeExams())
        disciplinesViewModel.liveDataDisciplineRemoval.observe(viewLifecycleOwner,
            observeDisciplineRemoval())

        configureViews()
        examsViewModel.getExams(id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeActivity?.showNavigation()
    }

    override fun onResume() {
        super.onResume()
        homeActivity?.configureActionButton(
            R.string.discipline_detail_fab,
            !examsAdapter.list.isNullOrEmpty(),
            clickListener
        )
    }

    override fun onPause() {
        super.onPause()
        homeActivity?.clearActionButton()
    }

    override fun onEmptyViewActionClick(view: View) {
        launchExamRegistrationActivity()
    }

    private fun configureViews() {
        discipline_toolbar_image_arrow.setOnClickListener {
            homeActivity?.onBackPressed()
        }

        discipline_toolbar_image_delete.setOnClickListener {
            disciplinesViewModel.removeDiscipline(id.orEmpty())
        }

        discipline_details_appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val isCollapsed = abs(verticalOffset) == appBarLayout.totalScrollRange
            discipline_toolbar_text_title.text = name.takeIf { isCollapsed }.orEmpty()
        })

        homeActivity?.hideNavigation()
        discipline_details_recyclerview.adapter = concatAdapter
        discipline_details_average_indicator.title(name).average(grade)
    }

    //region observers
    private fun observeDisciplineRemoval() = Observer<DataSource<List<Discipline>>> {
        if (it.dataState == DataState.SUCCESS) {
            activity?.onBackPressed()
            setFragmentResult(REQUEST_SUCCESS, bundleOf())
        }
    }

    private fun observeExams() = Observer<DataSource<List<Pair<Int?, List<Exam>>>>> {
        when (it.dataState) {
            DataState.LOADING -> {
                buildFullLoadingState()
            }

            DataState.SUCCESS -> {
                buildDisciplineDetails(it.data)
            }

            DataState.ERROR -> {

            }
        }
    }
    //endregion

    private fun buildEmptyState() {
        homeActivity?.hideFab()

        concatAdapter.removeAdapter(loadingAdapter)
        concatAdapter.removeAdapter(examsAdapter)

        concatAdapter.addAdapter(emptyExamsAdapter)
    }

    private fun buildFullLoadingState() {
        homeActivity?.hideFab()

        concatAdapter.removeAdapter(emptyExamsAdapter)
        concatAdapter.removeAdapter(examsAdapter)

        concatAdapter.addAdapter(loadingAdapter)
    }

    private fun addExams(list: List<Pair<Int?, List<Exam>>>) {
        homeActivity?.showFab()

        concatAdapter.removeAdapter(emptyExamsAdapter)
        concatAdapter.removeAdapter(loadingAdapter)

        examsAdapter.list = list
        concatAdapter.addAdapter(examsAdapter)
    }

    private fun buildDisciplineDetails(list: List<Pair<Int?, List<Exam>>>?) {
        if (list.isNullOrEmpty()) {
            buildEmptyState()
        } else {
            addExams(list)
            buildAverageIndicator(list)
        }
    }

    private fun buildAverageIndicator(list: List<Pair<Int?, List<Exam>>>) {
        val gradeResultList = list
            .map {
                it.second.mapNotNull { exam -> exam.grade_result }.sum()
            }
            .sortedDescending()
            .take(2)

        val average = if (gradeResultList.isEmpty()) null else gradeResultList.average().toFloat()


        discipline_details_average_indicator.average(average)
    }

    private fun launchExamRegistrationActivity() = id?.let {
        examRegistrationCallback.launchActivity<ExamRegistrationActivity>(
            context,
            EXTRA_DISCIPLINE_ID to it,
            EXTRA_CYCLES to homeActivity?.user?.graduation_info?.cycles
        )
    }

}